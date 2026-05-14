package com.janwypych.bankApiApplication.services;

import com.janwypych.bankApiApplication.entities.AccountEntity;
import com.janwypych.bankApiApplication.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

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

    public AccountEntity login(AccountEntity accountEntity) {
        Optional<AccountEntity> foundAccount = accountRepository.findByEmail(accountEntity.getEmail());
        
        if(foundAccount.isEmpty())
            throw new RuntimeException("Account not found");
        
        AccountEntity loggedAccount = foundAccount.get();
        
        if(!loggedAccount.getPassword().equals(accountEntity.getPassword()))
            throw new RuntimeException("Wrong Password");
        
        return loggedAccount;
    }
}
