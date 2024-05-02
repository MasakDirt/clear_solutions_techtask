package tech.task.clearsolutions.exception;

public class TokenExpiredException extends RuntimeException{

    public TokenExpiredException(String message) {
        super(message);
    }

}
