package com.emmanuela.fintechapplication.customExceptions;

public class FailedMailException extends RuntimeException{

    public FailedMailException(String message) {
        super(message);
    }
}

