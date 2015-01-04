package com.zhiyi.schedule.exception;

/**
 * Created by chanedi on 2014/12/24.
 */
public class InvalidIdException extends Exception {

    private static final String message = "传入了框架不允许的ID！";

    public InvalidIdException() {
        super(message);
    }

    public InvalidIdException(String message) {
        super(message);
    }

    public InvalidIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidIdException(Throwable cause) {
        super(message, cause);
    }

    protected InvalidIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
