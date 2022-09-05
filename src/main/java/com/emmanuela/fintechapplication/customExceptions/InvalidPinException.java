package com.emmanuela.fintechapplication.customExceptions;

public class InvalidPinException extends RuntimeException{
    public InvalidPinException(String message){
        super(message);
    }
}
