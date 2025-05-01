package restAssured.userAuth.jwt.userLogin.Domain.exception;

import java.time.LocalDateTime;

public class GenericExceptionResponse {
    private LocalDateTime localDateTime = LocalDateTime.now();
    private String statusCode;
    private String path;
    private String message;

    public GenericExceptionResponse(LocalDateTime localDateTime, String statusCode, String path, String message) {
        super();
        this.localDateTime = localDateTime;
        this.statusCode = statusCode;
        this.path = path;
        this.message = message;
    }

    public GenericExceptionResponse() {
        super();
    }

    public GenericExceptionResponse(String message, String statusCode) {
   super();
    this.message = message;
    this.statusCode = statusCode;

    }


    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GenericExceptionResponse{" +
                "localDateTime=" + localDateTime +
                ", statusCode='" + statusCode + '\'' +
                ", path='" + path + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
