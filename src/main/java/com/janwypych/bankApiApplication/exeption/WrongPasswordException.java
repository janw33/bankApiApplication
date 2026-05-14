package com.janwypych.bankApiApplication.exeption;

public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException(String message) { super(message);}
}
