package com.emmanuela.fintechapplication.customExceptions;

public class UsersNotFoundException extends RuntimeException{

    public UsersNotFoundException(String message) {
        super(message);
    }
}
