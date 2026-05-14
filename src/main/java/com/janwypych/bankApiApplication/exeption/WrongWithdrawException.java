package com.janwypych.bankApiApplication.exeption;

public class WrongWithdrawException extends RuntimeException{
    public WrongWithdrawException(String message) {super(message);}
}
