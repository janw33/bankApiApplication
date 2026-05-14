package com.janwypych.bankApiApplication.controllers;

import com.janwypych.bankApiApplication.Dto.*;
import com.janwypych.bankApiApplication.entities.AccountEntity;
import com.janwypych.bankApiApplication.mappers.AccountMapper;
import com.janwypych.bankApiApplication.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public AccountController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody CreateAccountRequest createAccountRequest) {
        AccountEntity account = accountMapper.mapFromCreateAccountRequest(createAccountRequest);
        AccountEntity addedAccount = accountService.addAccount(account);
        return new ResponseEntity<>(accountMapper.mapToAccountResponse(addedAccount), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AccountResponse> login(
            @RequestBody LoginRequest loginRequest) {
        AccountEntity accountEntity = accountMapper.mapFromLoginAccount(loginRequest);
        AccountEntity loggedAccount = accountService.login(accountEntity);
        return new ResponseEntity<>(accountMapper.mapToAccountResponse(loggedAccount), HttpStatus.OK);
    }

    @PatchMapping(path = "/deposit/{id}")
    public ResponseEntity<AccountResponse> deposit(
            @PathVariable("id") Long id,
            @RequestBody DepositRequest depositRequest) {
        AccountEntity accountEntity = accountService.deposit(id, depositRequest.getAmount());
        return new ResponseEntity<>(accountMapper.mapToAccountResponse(accountEntity), HttpStatus.OK);
    }
    @PatchMapping(path = "/withdraw/{id}")
    public ResponseEntity<AccountResponse> withdraw(
            @PathVariable("id") Long id,
            @RequestBody WithdrawRequest withdrawRequest) {
        AccountEntity accountEntity = accountService.withdraw(id, withdrawRequest.getAmount());
        return new ResponseEntity<>(accountMapper.mapToAccountResponse(accountEntity), HttpStatus.OK);
    }
}
