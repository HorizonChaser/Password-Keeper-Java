package io.github.horizonchaser;

public class JPKFileException extends RuntimeException {
    private JPKFileException() {
        super();
    }

    public  JPKFileException(String cause) {
        super(cause);
    }
}
