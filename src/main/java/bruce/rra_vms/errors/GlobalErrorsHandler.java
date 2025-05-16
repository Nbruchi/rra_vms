package bruce.rra_vms.errors;

import bruce.rra_vms.dto.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorsHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalErrorsHandler.class);
    @ExceptionHandler(OwnerNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleOwnerNotFound(OwnerNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidCredentialsException(InvalidCredentialsException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(PlateNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handlePlateNotFoundException(PlateNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(TransferNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleTransferNotFoundException(TransferNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleVehicleNotFoundException(VehicleNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException ex){
        String errorResponse = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .reduce("", (acc, fieldError) -> acc + fieldError + "\n");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex){
        logger.error("Unhandled exception occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse("An unexpected error occurred. Please try again later."));
    }
}
