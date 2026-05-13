package com.janwypych.bankApiApplication.services;

import com.janwypych.bankApiApplication.entities.AccountEntity;
import com.janwypych.bankApiApplication.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountEntity addAccount(AccountEntity accountEntity) {
        if(accountRepository.existsByEmail(accountEntity.getEmail()))
            throw new RuntimeException("Email already exists");
        accountEntity.setBalance(BigDecimal.ZERO);
        return accountRepository.save(accountEntity);
    }
}
