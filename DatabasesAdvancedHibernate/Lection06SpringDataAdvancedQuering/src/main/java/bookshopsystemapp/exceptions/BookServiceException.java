package bookshopsystemapp.exceptions;

public class BookServiceException extends Exception {

    BookServiceException() {

    }

    BookServiceException(String message) {
        super(message);
    }

    BookServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
