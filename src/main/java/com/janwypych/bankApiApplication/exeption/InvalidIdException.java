package com.janwypych.bankApiApplication.exeption;

public class InvalidIdException extends RuntimeException {
    public InvalidIdException(String message) { super(message); }
}

