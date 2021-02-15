package io.github.horizonchaser;

import java.io.*;
import java.util.Arrays;

public class LoginUtil {

    private static final transient byte[] loadedLoginHash = new byte[32];

    public static boolean loadLoginHashFromFile(String file) {
        try(InputStream inputStream = new FileInputStream(file)) {
            byte[] fileHead = new byte[CommonDefinition.fileHeaderSig.length];
            byte[] fileVer = new byte[CommonDefinition.fileHeadVer.length];
            inputStream.read(fileHead, 0, fileHead.length);
            inputStream.read(fileVer, 0, fileVer.length);

            if(!Arrays.equals(fileHead, CommonDefinition.fileHeaderSig)){
                throw new JPKFileException("File head signature not match");
            }
            if(!Arrays.equals(fileVer, CommonDefinition.fileHeadVer)) {
                throw new JPKFileException("File version not match");
            }

            inputStream.read(loadedLoginHash, 0, loadedLoginHash.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean checkAuth(byte[] hash) {
        return Arrays.equals(hash, loadedLoginHash);
    }

    public static boolean initializeNewSave() {
        return false;
    }
}
