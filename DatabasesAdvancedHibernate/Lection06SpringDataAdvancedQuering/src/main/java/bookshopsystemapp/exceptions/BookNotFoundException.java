package bookshopsystemapp.exceptions;

public class BookNotFoundException extends BookServiceException {

    BookNotFoundException() {

    }

    public BookNotFoundException(String message) {
        super(message);
    }

    BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
