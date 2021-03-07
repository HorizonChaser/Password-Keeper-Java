package io.github.horizonchaser;

/**
 * @author Horizon
 */
public class CommonDefinition {
    public static final byte[] FILE_HEADER_SIG = "JPK-Head".getBytes();
    public static final byte[] FILE_HEADER_VER = "0001".getBytes();
    public static final int FILE_HEADER_SIZE_IN_BYTE = FILE_HEADER_SIG.length + FILE_HEADER_VER.length + 32 + 4;

    public static final String DEFAULT_SAVE_NAME = "default_save.jpk";

    //XXX Disabled pswd complexity check for test only

    // public static final String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
    public static final String PASSWORD_PATTERN = "[1-9]\\d*";

    public static final String DEFAULT_CIPHER_INSTANCE = "AES/CBC/ISO10126Padding";
    public static final String ENCRYPT_ALGORITHM_NAME = "AES";
}
