package per.khalilov.crudproject.exceptions;

public class EntityAlreadyExistsException extends ApplicationRuntimeException {

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

}
