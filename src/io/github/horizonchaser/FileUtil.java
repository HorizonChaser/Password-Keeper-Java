package io.github.horizonchaser;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.CRC32;

public class FileUtil extends Main {
    public static void initializeNewDB(String path, byte[] key) {
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
                //TODO check result of delete and create

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

            for (byte b : CommonDefinition.fileHeaderSig) {
                byteList.add(b);
            }
            for (byte b : CommonDefinition.fileHeaderVer) {
                byteList.add(b);
            }
            for (byte b : Main.key) {
                byteList.add(b);
            }
            for (byte b : CryptoUtil.intToBytes(0)) {
                byteList.add(b);
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        Main.currSaveFilePath = file.getAbsolutePath();
    }

    public static void loadUserHashFromDB(File db) {
        if (!db.exists()) {
            throw new JPKFileException("File not exist: " + db.getAbsolutePath());
        }
        if (!db.canRead() || !db.canWrite()) {
            throw new JPKFileException("Permission denied on accessing " + db.getAbsolutePath());
        }
        if (db.length() <= CommonDefinition.fileHeaderSizeInByte) {
            throw new JPKFileException("File length not correct: at least " + (CommonDefinition.fileHeaderSizeInByte + 8)
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

        boolean isValidHeader = true;
        for (int i = 0; i < CommonDefinition.fileHeaderSig.length; i++) {
            if (buffer[i] != CommonDefinition.fileHeaderSig[i]) {
                isValidHeader = false;
                throw new JPKFileException("Invalid database file header");
            }
        }
        for (int i = 0; i < CommonDefinition.fileHeaderVer.length; i++) {
            if (buffer[i + CommonDefinition.fileHeaderSig.length] != CommonDefinition.fileHeaderVer[i]) {
                isValidHeader = false;
                throw new JPKFileException("Invalid database file version");
            }
        }

        CRC32 crc32 = new CRC32();
        crc32.reset();
        crc32.update(buffer);
        crc32Checksum = CryptoUtil.intToBytes((int) crc32.getValue());
        for (int i = 0; i < 4; i++) {
            if (crc32Buffer[i] != crc32Checksum[i]) {
                throw new JPKFileException("CRC32 checksum mismatch: expected "
                        + CryptoUtil.bytesToHex(crc32Checksum) + " but " + CryptoUtil.bytesToHex(crc32Buffer) + " in actual");
            }
        }

        System.arraycopy(buffer, 0xC, Main.key, 0, 32);
        byte[] entryCntBytes = new byte[4];
        System.arraycopy(buffer, 0x2C, entryCntBytes, 0, 4);
        Main.currEntryCnt = CryptoUtil.bytesToInt(entryCntBytes);

    }
}
