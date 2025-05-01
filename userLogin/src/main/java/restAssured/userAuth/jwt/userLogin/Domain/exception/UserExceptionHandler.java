package restAssured.userAuth.jwt.userLogin.Domain.exception;

public class UserExceptionHandler extends RuntimeException{

    public UserExceptionHandler(String message) {
        super(message);
    }
}
