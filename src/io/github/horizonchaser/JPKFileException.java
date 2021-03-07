package io.github.horizonchaser;

/**
 * @author Horizon
 */
public class JPKFileException extends RuntimeException {
    private JPKFileException() {
        super();
    }

    public JPKFileException(String cause) {
        super(cause);
    }
}
