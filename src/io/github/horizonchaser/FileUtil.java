package io.github.horizonchaser;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.converter.ByteStringConverter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.CRC32;

public class FileUtil {
    public static boolean initializeDB(String path, byte[] key) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    Alert createFailed = new Alert(Alert.AlertType.ERROR, "Failed to create new save file as "
                            + path);
                    createFailed.showAndWait();
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert confirmOverwrite = new Alert(Alert.AlertType.CONFIRMATION, "File already exists, overwrite?");
            Optional<ButtonType> result =  confirmOverwrite.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                //TODO check result of delete and create

                file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(!file.canRead() || !file.canWrite()) {
            Alert fileNotValid = new Alert(Alert.AlertType.ERROR, "Current save file not available."
                    + "\nThis may indicate permission problems. Program will exit.");
            fileNotValid.showAndWait();
            System.exit(-1);
        }

        try (OutputStream outputStream = new FileOutputStream(file)){
            List<Byte> byteList = new ArrayList<>();

            for (byte b : CommonDefinition.fileHeaderSig) {
                byteList.add(b);
            }
            for (byte b : CommonDefinition.fileHeadVer) {
                byteList.add(b);
            }
            for (byte b : Main.key) {
                byteList.add(b);
            }
            for(byte b : CryptoUtil.int2Bytes(0)){
                byteList.add(b);
            }

            CRC32 crc32 = new CRC32();
            crc32.reset();
            int contentLength = byteList.size();
            byte[] buffer = new byte[byteList.size() + 4];
            for(int i = 0;i < byteList.size();i++) {
                buffer[i] = byteList.get(i);
            }
            crc32.update(buffer, 0, byteList.size());
            byte[] crcBytes = CryptoUtil.int2Bytes((int)crc32.getValue());

            System.out.println(CryptoUtil.bytes2Hex(crcBytes));

            for(int i = 0;i < crcBytes.length;i++) {
                byteList.add(crcBytes[i]);
                buffer[contentLength+i] = crcBytes[i];
            }

            outputStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
