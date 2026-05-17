package com.janwypych.bankApiApplication.Dto;

import com.janwypych.bankApiApplication.entities.enums.AccountStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeStatusRequest {
    @NotNull
    private AccountStatus status;
}
