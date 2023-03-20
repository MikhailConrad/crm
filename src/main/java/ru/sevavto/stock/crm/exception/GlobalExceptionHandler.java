package ru.sevavto.stock.crm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.sevavto.stock.crm.model.dto.IncorrectDataResponse;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validationException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage() + ": " + ((FieldError) error).getRejectedValue();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<IncorrectDataResponse> notFound(NotFoundException exception) {
        IncorrectDataResponse incorrectDataResponse = new IncorrectDataResponse(exception.getMessage());
        return new ResponseEntity<>(incorrectDataResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StatusChangingException.class)
    public ResponseEntity<IncorrectDataResponse> notValidStatus(StatusChangingException exception) {
        IncorrectDataResponse incorrectDataResponse = new IncorrectDataResponse(exception.getMessage());
        return new ResponseEntity<>(incorrectDataResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExcessOfAvailableQuantityException.class)
    public ResponseEntity<IncorrectDataResponse> excessOfAvailableQuantity(ExcessOfAvailableQuantityException exception) {
        IncorrectDataResponse incorrectDataResponse = new IncorrectDataResponse(exception.getMessage());
        return new ResponseEntity<>(incorrectDataResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Exception> otherException(Exception exception) {
        return new ResponseEntity<>(exception, HttpStatus.valueOf(500));
    }
}
