package per.khalilov.crudproject.aspects;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import per.khalilov.crudproject.dto.response.ExceptionDto;
import per.khalilov.crudproject.dto.response.ValidationExceptionDto;
import per.khalilov.crudproject.exceptions.ApplicationRuntimeException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ValidationExceptionDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ValidationExceptionDto> exceptions = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String message = error.getDefaultMessage();
            String fieldName = ((FieldError) error).getField();
            exceptions.add(ValidationExceptionDto.builder()
                            .field(fieldName)
                            .message(message)
                    .build());
        });
        return exceptions;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ValidationExceptionDto> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ValidationExceptionDto> exceptions = new ArrayList<>();
        ex.getConstraintViolations().forEach(error -> {
            String message = error.getMessage();
            exceptions.add(ValidationExceptionDto.builder()
                    .message(message)
                    .build());
        });
        return exceptions;
    }

    @ExceptionHandler(ApplicationRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleApplicationRuntimeException(ApplicationRuntimeException ex) {
        return ExceptionDto.builder()
                .message(ex.getMessage())
                .exceptionName(ex.getClass().getSimpleName())
                .build();
    }

}
