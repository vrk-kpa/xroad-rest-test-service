package fi.dvv.xroad.resttestservice.error;

public class ValidationException extends IllegalArgumentException {
    public ValidationException(String errorMessage) {
        super(errorMessage);
    }
}
