package io.github.horizonchaser;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.CRC32;

public class FileUtil extends Main {

    static byte[] entryEncryptBytes;

    public static void saveDBToFile(List<RecordEntry> recordEntryList, String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    Alert createFailed = new Alert(Alert.AlertType.ERROR, "Failed to create new save file as "
                            + path);
                    createFailed.showAndWait();
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert confirmOverwrite = new Alert(Alert.AlertType.CONFIRMATION, "File already exists, overwrite?");
            Optional<ButtonType> result = confirmOverwrite.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                //XXX check result of delete and create
                file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!file.canRead() || !file.canWrite()) {
            Alert fileNotValid = new Alert(Alert.AlertType.ERROR, "Current save file not available."
                    + "\nThis may indicate permission problems. Program will exit.");
            fileNotValid.showAndWait();
            System.exit(-1);
        }

        try (OutputStream outputStream = new FileOutputStream(file)) {
            List<Byte> byteList = new ArrayList<>();

            for (byte b : CommonDefinition.FILE_HEADER_SIG) {
                byteList.add(b);
            }
            for (byte b : CommonDefinition.FILE_HEADER_VER) {
                byteList.add(b);
            }
            for (byte b : Main.userHash) {
                byteList.add(b);
            }

            if (recordEntryList.size() > 0) {
                for (byte b : CryptoUtil.intToBytes(recordEntryList.size())) {
                    byteList.add(b);
                }

                Cipher cipher = Cipher.getInstance(CommonDefinition.DEFAULT_CIPHER_INSTANCE);
                SecretKeySpec sKeySpec = new SecretKeySpec(Main.dataKey, CommonDefinition.ENCRYPT_ALGORITHM_NAME);
                cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, new IvParameterSpec(new byte[16]));

                StringBuilder contentBuilder = new StringBuilder();
                for (RecordEntry currEntry : recordEntryList) {
                    contentBuilder.append(currEntry.getDomain());
                    contentBuilder.append('\n');
                    contentBuilder.append(currEntry.getUsername());
                    contentBuilder.append('\n');
                    contentBuilder.append(currEntry.getPassword());
                    contentBuilder.append('\n');
                    contentBuilder.append(currEntry.getNote());
                    contentBuilder.append('\r');
                }

                for (byte b : cipher.doFinal(contentBuilder.toString().getBytes())) {
                    byteList.add(b);
                }
            } else {
                for (byte b : CryptoUtil.intToBytes(0)) {
                    byteList.add(b);
                }
            }

            CRC32 crc32 = new CRC32();
            crc32.reset();
            int contentLength = byteList.size();
            byte[] buffer = new byte[byteList.size() + 4];
            for (int i = 0; i < byteList.size(); i++) {
                buffer[i] = byteList.get(i);
            }
            crc32.update(buffer, 0, byteList.size());
            byte[] crcBytes = CryptoUtil.intToBytes((int) crc32.getValue());

            System.out.println(CryptoUtil.bytesToHex(crcBytes));

            for (int i = 0; i < crcBytes.length; i++) {
                byteList.add(crcBytes[i]);
                buffer[contentLength + i] = crcBytes[i];
            }

            outputStream.write(buffer);
        } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        Main.currSaveFilePath = file.getAbsolutePath();
    }

    public static void verifyAndLoadUserHash(File db) {
        if (!db.exists()) {
            throw new JPKFileException("File not exist: " + db.getAbsolutePath());
        }
        if (!db.canRead() || !db.canWrite()) {
            throw new JPKFileException("Permission denied on accessing " + db.getAbsolutePath());
        }
        if (db.length() <= CommonDefinition.FILE_HEADER_SIZE_IN_BYTE) {
            throw new JPKFileException("File length not correct: at least " + (CommonDefinition.FILE_HEADER_SIZE_IN_BYTE + 8)
                    + " but " + db.length() + " in actual");
        }

        byte[] buffer = new byte[(int) (db.length() - 4)];
        byte[] crc32Buffer = new byte[4];
        byte[] crc32Checksum;

        try {
            InputStream inputStream = new FileInputStream(db);
            inputStream.read(buffer, 0, buffer.length);
            inputStream.read(crc32Buffer, 0, crc32Buffer.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < CommonDefinition.FILE_HEADER_SIG.length; i++) {
            if (buffer[i] != CommonDefinition.FILE_HEADER_SIG[i]) {
                throw new JPKFileException("Invalid database file header");
            }
        }
        for (int i = 0; i < CommonDefinition.FILE_HEADER_VER.length; i++) {
            if (buffer[i + CommonDefinition.FILE_HEADER_SIG.length] != CommonDefinition.FILE_HEADER_VER[i]) {
                throw new JPKFileException("Invalid database file version");
            }
        }

        CRC32 crc32 = new CRC32();
        crc32.reset();
        crc32.update(buffer);
        crc32Checksum = CryptoUtil.intToBytes((int) crc32.getValue());
        for (int i = 0; i < crc32Buffer.length; i++) {
            if (crc32Buffer[i] != crc32Checksum[i]) {
                throw new JPKFileException("CRC32 checksum mismatch: expected "
                        + CryptoUtil.bytesToHex(crc32Checksum) + " but " + CryptoUtil.bytesToHex(crc32Buffer) + " in actual");
            }
        }

        System.arraycopy(buffer, 0xC, Main.userHash, 0, 32);
        byte[] entryCntBytes = new byte[4];
        System.arraycopy(buffer, 0x2C, entryCntBytes, 0, 4);
        Main.currEntryCnt = CryptoUtil.bytesToInt(entryCntBytes);

        if (Main.currEntryCnt == 0) {
            return; //no entry exist, just return
        }

        entryEncryptBytes = new byte[(int) (db.length() - 4 - CommonDefinition.FILE_HEADER_SIZE_IN_BYTE)];
        System.arraycopy(buffer, 0x30, entryEncryptBytes, 0, entryEncryptBytes.length);
    }

    public static void decryptEntryListBytes() {
        try {
            SecretKeySpec sKeySpec = new SecretKeySpec(Main.dataKey, CommonDefinition.ENCRYPT_ALGORITHM_NAME);
            Cipher cipher = Cipher.getInstance(CommonDefinition.DEFAULT_CIPHER_INSTANCE);
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, new IvParameterSpec(new byte[16]));
            entryEncryptBytes = cipher.doFinal(entryEncryptBytes);
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException
                | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        String content = new String(entryEncryptBytes);
        String[] entryArray = content.split("\r");
        for (String currEntry : entryArray) {
            String[] currEntrySplit = currEntry.split("\n");
            Main.recordEntryList.add(
                    new RecordEntry(currEntrySplit[0], currEntrySplit[1], currEntrySplit[2], currEntrySplit[3])
            );
        }
    }
}
