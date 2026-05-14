package com.janwypych.bankApiApplication;

import com.janwypych.bankApiApplication.Dto.CreateAccountRequest;
import com.janwypych.bankApiApplication.Dto.DepositRequest;
import com.janwypych.bankApiApplication.Dto.LoginRequest;
import com.janwypych.bankApiApplication.entities.AccountEntity;

import java.math.BigDecimal;

public final class TestDataUtil {

    public static AccountEntity createAccountEntity() {
        return AccountEntity.builder()
                .firstName("Jan")
                .lastName("Wypych")
                .email("janWypych@email.com")
                .password("password")
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
                .amount(BigDecimal.valueOf(10))
                .build();
    }
}
