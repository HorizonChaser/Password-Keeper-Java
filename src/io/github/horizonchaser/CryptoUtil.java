package io.github.horizonchaser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoUtil {

    private transient static final byte[] loginSalt ={119, 101, 64, 107, 67, 114, 121, 113, 116, 48};

    public static byte[] calUserLoginHash(String username, String password) {
        byte[] hash = null, usernameByte = username.getBytes(), passwordByte = password.getBytes();
        MessageDigest md5Digest = null, sha256Digest = null;

        try {
            md5Digest = MessageDigest.getInstance("MD5");
            sha256Digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        /*
        byte[] unhashed = new byte[usernameByte.length + passwordByte.length + loginSalt.length];
        System.arraycopy(usernameByte, 0, unhashed, 0, usernameByte.length);
        System.arraycopy(passwordByte, 0, unhashed, usernameByte.length, passwordByte.length);
        System.arraycopy(loginSalt, 0, unhashed, passwordByte.length + usernameByte.length, loginSalt.length);
        */

        if (md5Digest != null) {
            md5Digest.update(usernameByte);
            md5Digest.update(passwordByte);
            md5Digest.update(loginSalt);
            hash = md5Digest.digest();
        }
        
        if(sha256Digest != null) {
            sha256Digest.update(hash);
            sha256Digest.update(loginSalt);
            hash = sha256Digest.digest();
        }

        return hash;
    }
}
