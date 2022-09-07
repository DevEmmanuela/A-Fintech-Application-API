package com.emmanuela.fintechapplication.customExceptions;

public class InvalidBVNException extends RuntimeException{
    public InvalidBVNException(String message) {
        super(message);
    }
}
