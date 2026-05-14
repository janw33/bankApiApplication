package com.janwypych.bankApiApplication;

import com.janwypych.bankApiApplication.Dto.*;
import com.janwypych.bankApiApplication.entities.AccountEntity;

import java.math.BigDecimal;

public final class TestDataUtil {

    public static AccountEntity createAccountEntity1() {
        return AccountEntity.builder()
                .firstName("Jan")
                .lastName("Wypych")
                .email("janWypych@email.com")
                .password("password")
                .build();
    }
    public static AccountEntity createAccountEntity2() {
        return AccountEntity.builder()
                .firstName("123")
                .lastName("123")
                .email("123@email.com")
                .password("123")
                .build();
    }
    public static CreateAccountRequest createCreateAccountRequest() {
        return CreateAccountRequest.builder()
                .firstName("Jan")
                .lastName("Wypych")
                .email("janWypych@email.com")
                .password("password")
                .build();
    }
    public static LoginRequest createLoginRequest() {
        return LoginRequest.builder()
                .email("janWypych@email.com")
                .password("password")
                .build();
    }
    public static DepositRequest createDepositRequest() {
        return DepositRequest.builder()
                .amount(BigDecimal.TEN)
                .build();
    }
    public static WithdrawRequest createWithdrawRequest() {
        return WithdrawRequest.builder()
                .amount(BigDecimal.TEN)
                .build();
    }
    public static TransferRequest createTransferRequest() {
        return TransferRequest.builder()
                .senderId(1L)
                .receiverId(2L)
                .amount(BigDecimal.TEN)
                .build();
    }
}
