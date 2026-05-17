package com.janwypych.bankApiApplication.exception;

public class WrongWithdrawException extends RuntimeException{
    public WrongWithdrawException(String message) {super(message);}
}
