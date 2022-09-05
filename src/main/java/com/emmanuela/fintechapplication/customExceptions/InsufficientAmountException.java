package com.emmanuela.fintechapplication.customExceptions;

public class InsufficientAmountException extends RuntimeException {
    public InsufficientAmountException(String message) {
        super(message);
    }
}
