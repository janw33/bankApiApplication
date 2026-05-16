package com.janwypych.bankApiApplication.Dto;

import com.janwypych.bankApiApplication.entities.AccountEntity;
import com.janwypych.bankApiApplication.entities.enums.TransactionTypeEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
    private Long id;
    private BigDecimal amount;
    private TransactionTypeEnum type;
    private LocalDateTime time;
    private AccountEntity senderAccount;
    private Long receiverId;
}