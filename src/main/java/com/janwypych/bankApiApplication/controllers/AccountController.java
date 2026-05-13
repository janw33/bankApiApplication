package com.janwypych.bankApiApplication.controllers;

import com.janwypych.bankApiApplication.Dto.AccountResponse;
import com.janwypych.bankApiApplication.Dto.CreateAccountRequest;
import com.janwypych.bankApiApplication.entities.AccountEntity;
import com.janwypych.bankApiApplication.mappers.AccountMapper;
import com.janwypych.bankApiApplication.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public AccountController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @PostMapping(path = "/accounts")
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody CreateAccountRequest createAccountRequest) {
        AccountEntity accountEntity = accountMapper.mapFromCreateAccountRequest(createAccountRequest);
        AccountEntity addedAccount = accountService.addAccount(accountEntity);
        return new ResponseEntity<>(accountMapper.mapToAccountResponse(addedAccount), HttpStatus.CREATED);
    }
}
