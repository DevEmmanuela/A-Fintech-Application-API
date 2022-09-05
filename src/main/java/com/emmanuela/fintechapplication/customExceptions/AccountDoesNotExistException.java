package com.emmanuela.fintechapplication.customExceptions;

public class AccountDoesNotExistException extends RuntimeException{

    public AccountDoesNotExistException(String message) {
        super(message);
    }
}
