package com.janwypych.bankApiApplication.exeption;

public class WrongTransferAmountException extends RuntimeException{
    public WrongTransferAmountException(String message) {super(message);}
}
