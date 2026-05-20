package com.janwypych.bankApiApplication.controllers;

import com.janwypych.bankApiApplication.Dto.*;
import com.janwypych.bankApiApplication.entities.AccountEntity;
import com.janwypych.bankApiApplication.mappers.AccountMapper;
import com.janwypych.bankApiApplication.services.AccountService;
import com.janwypych.bankApiApplication.services.JwtService;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final JwtService jwtService;

    public AccountController(AccountService accountService, AccountMapper accountMapper, JwtService jwtService) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
        this.jwtService = jwtService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<AuthResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest createAccountRequest) {

        AccountEntity account = accountMapper.mapFromCreateAccountRequest(createAccountRequest);
        AccountEntity addedAccount = accountService.addAccount(account);

        String token = jwtService.generateToken(addedAccount.getId());

        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        AccountEntity accountEntity = accountMapper.mapFromLoginAccount(loginRequest);
        AccountEntity loggedAccount = accountService.login(accountEntity);

        String token = jwtService.generateToken(loggedAccount.getId());

        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }

    @PatchMapping(path = "/deposit/{id}")
    public ResponseEntity<AccountResponse> deposit(
            @PathVariable("id") Long id,
            @Valid @RequestBody DepositRequest depositRequest) {
        AccountEntity accountEntity = accountService.deposit(id, depositRequest.getAmount());
        return new ResponseEntity<>(accountMapper.mapToAccountResponse(accountEntity), HttpStatus.OK);
    }
    @PatchMapping(path = "/withdraw/{id}")
    public ResponseEntity<AccountResponse> withdraw(
            @PathVariable("id") Long id,
            @Valid @RequestBody WithdrawRequest withdrawRequest) {
        AccountEntity accountEntity = accountService.withdraw(id, withdrawRequest.getAmount());
        return new ResponseEntity<>(accountMapper.mapToAccountResponse(accountEntity), HttpStatus.OK);
    }
    @PatchMapping(path = "/transfer")
    public ResponseEntity<AccountResponse> transfer(
            @Valid @RequestBody TransferRequest transferRequest ) {
        AccountEntity accountEntity = accountService.transfer(
                transferRequest.getSenderId(),
                transferRequest.getReceiverId(),
                transferRequest.getAmount());
        return new ResponseEntity<>(accountMapper.mapToAccountResponse(accountEntity), HttpStatus.OK);
    }
    @PatchMapping(path = "/accounts/{id}/status")
    private ResponseEntity<AccountResponse> changeStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangeStatusRequest changeStatusRequest
    ) {
        AccountEntity accountEntity = accountService.changeStatus(id, changeStatusRequest.getStatus());
        return new ResponseEntity<>(accountMapper.mapToAccountResponse(accountEntity), HttpStatus.OK);
    }
}
