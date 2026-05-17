package com.janwypych.bankApiApplication.exeption;

public class WrongTransferException extends RuntimeException{
    public WrongTransferException(String message) {super(message);}
}
