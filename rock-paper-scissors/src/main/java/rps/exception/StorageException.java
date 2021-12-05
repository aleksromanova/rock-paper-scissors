package rps.exception;

/**
 * Signals about problem with storage
 */
public class StorageException extends Exception {
    public StorageException() {
        super();
    }

    public StorageException(String message) {
        super(message);
    }
}
