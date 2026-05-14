package com.janwypych.bankApiApplication.exeption;

public class WrongDepositException extends RuntimeException{
    public WrongDepositException(String message) {super(message);}
}
