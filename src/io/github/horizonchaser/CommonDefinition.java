package io.github.horizonchaser;

public class CommonDefinition {
    public static final byte[] fileHeaderSig = "JPK-Head".getBytes();
    public static final byte[] fileHeaderVer = "0001".getBytes();
    public static final int fileHeaderSizeInByte = fileHeaderSig.length + fileHeaderVer.length + 32 + 4;

    public static final String defaultSaveName = "saved.jpk";

    //TODO: Disabled pswd complexity check for test only

    // public static final String passwordPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
    public static final String passwordPattern = "[1-9]\\d*";
}
