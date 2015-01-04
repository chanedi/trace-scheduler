package com.zhiyi.schedule.exception;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;


public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 2925961769468414327L;
    private static Logger logger = Logger.getLogger(BaseException.class);
    private Exception originException;


    public static void logForException(String message,Exception e) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Exception message: \n").append(message).append("\n");
        stringBuilder.append("Exception stack trace: \n");
        for (StackTraceElement element : e.getStackTrace()) {
            stringBuilder.append(element.toString()).append("\n");
        }

        if (e.getCause() != null) {
            stringBuilder.append("Exception cause: \n").append(e.getCause().toString()).append("\n");
        }
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error(stringBuilder);
        }
    }

    public BaseException(HashMap<Object, Object> arguments, Exception e) {
        originException = e;
        StringBuilder stringBuilder = new StringBuilder(generateArgumentErrorMessage(arguments));
        stringBuilder.append(generalExceptionMessage());
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error(stringBuilder);
        }
    }

    public BaseException(HashMap<Object, Object> arguments, String message, Exception e) {
        super(message, e);
        originException = e;
        StringBuilder stringBuilder = new StringBuilder(generateArgumentErrorMessage(arguments));
        stringBuilder.append(generalExceptionMessage());
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error(stringBuilder);
        }
    }

    public BaseException(String message) {
        super(message);
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error(generalExceptionMessage());
        }
    }


    public BaseException(Exception e) {
        super(e);
        originException = e;
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error(generalExceptionMessage());
        }
    }

    public BaseException(String message, Exception e) {
        super(message, e);
        originException = e;
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error(generalExceptionMessage());
        }
    }


    protected String generalExceptionMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Exception toString: \n").append(this.toString()).append("\n");
        stringBuilder.append("Exception stack trace: \n");

        if (originException != null) {
            for (StackTraceElement element : originException.getStackTrace()) {
                stringBuilder.append(element.toString()).append("\n");
            }

            if (originException.getCause() != null) {
                stringBuilder.append("Exception cause: \n").append(originException.getCause().toString()).append("\n");
            }
        }

        return stringBuilder.toString();
    }

    private String generateArgumentErrorMessage(HashMap<Object, Object> arguments) {
        StringBuilder stringBuilder = new StringBuilder("arguments:").append("\n");
        for (Map.Entry<Object, Object> entry : arguments.entrySet()) {
            stringBuilder.append("key: ").append(entry.getKey().toString());
            stringBuilder.append(" value: ").append(entry.getValue().toString()).append("\n");
        }
        stringBuilder.append("exception message:").append("\n");
        return stringBuilder.toString();
    }
}
