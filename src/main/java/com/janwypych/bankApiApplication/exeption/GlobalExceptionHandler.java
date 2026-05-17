package com.janwypych.bankApiApplication.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.CONFLICT
        );
    }
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFound(
            AccountNotFoundException anf) {

        return new ResponseEntity<>(
                anf.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<String> handleWrongPassword(
            WrongPasswordException wpe) {

        return new ResponseEntity<>(
                wpe.getMessage(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(WrongWithdrawException.class)
    public ResponseEntity<String> handleWrongWithdraw(
            WrongWithdrawException wwe) {

        return new ResponseEntity<>(
                wwe.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(WrongTransferException.class)
    public ResponseEntity<String> handleWrongTransferAmount(
            WrongTransferException wtae) {

        return new ResponseEntity<>(
                wtae.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<String> handleWrongSenderReceiverIdException(
            InvalidIdException iie) {

        return new ResponseEntity<>(
                iie.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }
}
