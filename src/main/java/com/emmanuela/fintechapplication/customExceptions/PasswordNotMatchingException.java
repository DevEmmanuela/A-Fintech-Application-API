package com.emmanuela.fintechapplication.customExceptions;

public class PasswordNotMatchingException extends RuntimeException{

    public PasswordNotMatchingException(String message) {
        super(message);
    }
}
