package io.github.horizonchaser;

public class CommonDefinition {
    public static final byte[] fileHeaderSig = "JPK-Head".getBytes();
    public static final byte[] fileHeadVer = "0.0.1".getBytes();

    public static final String defaultSaveName = "saved.jpk";

    // public static final String passwordPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
    public static final String passwordPattern = "";
}
