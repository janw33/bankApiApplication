package com.janwypych.bankApiApplication.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest {
    @NotNull
    @Positive
    Long receiverId;

    @NotNull
    @Positive
    Long senderId;

    @NotNull
    @Positive
    BigDecimal amount;
}
