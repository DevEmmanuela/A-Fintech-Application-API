package com.emmanuela.fintechapplication.customExceptions;

public class WalletNotFoundException extends Exception{
    public WalletNotFoundException(String message) {
        super(message);
    }
}
