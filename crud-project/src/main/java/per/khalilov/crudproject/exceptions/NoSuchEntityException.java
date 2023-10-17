package per.khalilov.crudproject.exceptions;

public class NoSuchEntityException extends ApplicationRuntimeException {

    public NoSuchEntityException(String message) {
        super(message);
    }

}
