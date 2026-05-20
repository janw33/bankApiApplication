package com.janwypych.bankApiApplication.exception;

import com.janwypych.bankApiApplication.Dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailAlreadyExists(
            EmailAlreadyExistsException emailAlreadyExistsException,
            HttpServletRequest request) {

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("EMAIL_ALREADY_EXISTS")
                .message(emailAlreadyExistsException.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.CONFLICT
        );
    }
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleAccountNotFound(
            AccountNotFoundException accountNotFoundException,
            HttpServletRequest request) {

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("ACCOUNT_NOT_FOUND")
                .message(accountNotFoundException.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ErrorResponseDto> handleWrongPassword(
            WrongPasswordException wrongPasswordException,
            HttpServletRequest request) {

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("WRONG_PASSWORD")
                .message(wrongPasswordException.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(WrongWithdrawException.class)
    public ResponseEntity<ErrorResponseDto> handleWrongWithdraw(
            WrongWithdrawException wrongWithdrawException,
            HttpServletRequest request) {

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("WRONG_WITHDRAW")
                .message(wrongWithdrawException.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(WrongTransferException.class)
    public ResponseEntity<ErrorResponseDto> handleWrongTransferAmount(
            WrongTransferException wrongTransferException,
            HttpServletRequest request) {

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("WRONG_TRANSFER")
                .message(wrongTransferException.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<ErrorResponseDto> handleWrongSenderReceiverIdException(
            InvalidIdException invalidIdException,
            HttpServletRequest request) {

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("INVALID_ID")
                .message(invalidIdException.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(AccountIsInactiveException.class)
    public ResponseEntity<ErrorResponseDto> handeAccountIsInactiveException(
            AccountIsInactiveException accountIsInactiveException,
            HttpServletRequest request ) {

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("ACCOUNT_IS_INACTIVE")
                .message(accountIsInactiveException.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidTokenException(
            InvalidTokenException invalidTokenException,
            HttpServletRequest request ) {

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("INVALID_TOKEN")
                .message(invalidTokenException.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.UNAUTHORIZED
        );
    }
}
