package io.github.horizonchaser;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoUtil {

    private transient static final byte[] loginSalt = {119, 101, 64, 107, 67, 114, 121, 113, 116, 48};

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

        if (sha256Digest != null) {
            sha256Digest.update(hash);
            sha256Digest.update(loginSalt);
            hash = sha256Digest.digest();
        }

        return hash;
    }

    /**
     * byte[] to hex string
     *
     * @param bytes source
     * @return hex string
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder des = new StringBuilder();
        String tmp;
        for (byte bt : bytes) {
            tmp = (Integer.toHexString(bt & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }

    /**
     * int to byte[]
     *
     * @param integer source
     * @return byte[], equals to integer
     */
    public static byte[] intToBytes(int integer) {
        byte[] bytes = new byte[4];
        bytes[3] = (byte) (integer >> 24);
        bytes[2] = (byte) (integer >> 16);
        bytes[1] = (byte) (integer >> 8);
        bytes[0] = (byte) (integer);

        return bytes;
    }

    //byte 数组与 int 的相互转换
    public static int bytesToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    /**
     * long to byte[]
     *
     * @param x source
     * @return byte[], equals to x
     */
    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

}
