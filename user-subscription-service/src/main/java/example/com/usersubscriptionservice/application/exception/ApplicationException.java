package example.com.usersubscriptionservice.application.exception;

public abstract class ApplicationException extends RuntimeException  {
    public ApplicationException(String message) {
        super(message);
    }
}
