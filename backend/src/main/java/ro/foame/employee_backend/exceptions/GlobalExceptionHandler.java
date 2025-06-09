package ro.foame.employee_backend.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) {
        exception.printStackTrace();

        HttpStatus status;
        String description;

        switch (exception.getClass().getSimpleName()) {
            case "BadCredentialsException" -> {
                status = HttpStatus.UNAUTHORIZED;
                description = "The username or password is incorrect";
            }
            case "AccountStatusException" -> {
                status = HttpStatus.FORBIDDEN;
                description = "The account is locked";
            }
            case "AccessDeniedException" -> {
                status = HttpStatus.FORBIDDEN;
                description = "You are not authorized to access this resource";
            }
            case "SignatureException" -> {
                status = HttpStatus.FORBIDDEN;
                description = "The JWT signature is invalid";
            }
            case "ExpiredJwtException" -> {
                status = HttpStatus.FORBIDDEN;
                description = "The JWT token has expired";
            }
            case "EntityExistsException" -> {
                status = HttpStatus.CONFLICT;
                description = "The entity already exists";
            }
            default -> {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                description = "Unknown internal server error.";
            }
        }

        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
        errorDetail.setProperty("description", description);

        return errorDetail;
    }
}
