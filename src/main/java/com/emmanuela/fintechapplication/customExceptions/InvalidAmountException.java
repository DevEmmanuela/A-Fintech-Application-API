package com.emmanuela.fintechapplication.customExceptions;

public class InvalidAmountException extends RuntimeException{

    public InvalidAmountException(String message) {
        super(message);
    }
}
