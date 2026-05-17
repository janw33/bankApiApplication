package com.janwypych.bankApiApplication.Dto;


import com.janwypych.bankApiApplication.entities.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal balance;
    private AccountStatus status;
}
