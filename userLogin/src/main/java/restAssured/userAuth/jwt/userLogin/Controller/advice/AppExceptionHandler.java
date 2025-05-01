package restAssured.userAuth.jwt.userLogin.Controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import restAssured.userAuth.jwt.userLogin.Domain.exception.GenericExceptionResponse;
import restAssured.userAuth.jwt.userLogin.Domain.exception.UserExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(value = UserExceptionHandler.class)
    public ResponseEntity<GenericExceptionResponse> handleValidationException(UserExceptionHandler userException,
                                                                              HttpServletRequest httpRequest) {
        GenericExceptionResponse response = new GenericExceptionResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.toString(),
                httpRequest.getRequestURI(), userException.getMessage());

        return new ResponseEntity<GenericExceptionResponse>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
