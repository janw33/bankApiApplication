package com.janwypych.bankApiApplication;

import com.janwypych.bankApiApplication.Dto.*;
import com.janwypych.bankApiApplication.entities.AccountEntity;
import com.janwypych.bankApiApplication.entities.TransactionEntity;
import com.janwypych.bankApiApplication.entities.enums.AccountStatus;
import com.janwypych.bankApiApplication.entities.enums.TransactionTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    public static TransactionEntity createTransactionEntity(AccountEntity senderAccount) {
        return TransactionEntity
                .builder()
                .id(1L)
                .amount(BigDecimal.TEN)
                .type(TransactionTypeEnum.DEPOSIT)
                .time(LocalDateTime.now())
                .senderAccount(senderAccount)
                .receiverAccount(null)
                .build();
    }
    public static ChangeStatusRequest createChangeStatusRequest() {
        return ChangeStatusRequest.builder()
                .status(AccountStatus.INACTIVE)
                .build();
    }
}
