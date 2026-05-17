package com.janwypych.bankApiApplication.exception;

public class AccountIsInactiveException extends RuntimeException{
    public AccountIsInactiveException(String message) { super (message);}
}
