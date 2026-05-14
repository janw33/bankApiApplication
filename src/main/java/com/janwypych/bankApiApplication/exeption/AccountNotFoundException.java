package com.janwypych.bankApiApplication.exeption;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String message) { super (message);}
}
