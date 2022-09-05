package com.emmanuela.fintechapplication.customExceptions;

public class EmailAlreadyConfirmedException extends RuntimeException{
    public EmailAlreadyConfirmedException (String message) {
        super(message);
    }
}
