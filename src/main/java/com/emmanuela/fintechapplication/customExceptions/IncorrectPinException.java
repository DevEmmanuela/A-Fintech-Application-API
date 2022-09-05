package com.emmanuela.fintechapplication.customExceptions;

public class IncorrectPinException extends RuntimeException{

    public IncorrectPinException(String message) {
        super(message);
    }
}
