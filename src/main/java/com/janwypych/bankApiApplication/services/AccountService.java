package com.janwypych.bankApiApplication.services;

import com.janwypych.bankApiApplication.entities.AccountEntity;
import com.janwypych.bankApiApplication.exeption.AccountNotFoundException;
import com.janwypych.bankApiApplication.exeption.EmailAlreadyExistsException;
import com.janwypych.bankApiApplication.exeption.WrongDepositException;
import com.janwypych.bankApiApplication.exeption.WrongPasswordException;
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
            throw new EmailAlreadyExistsException("Email already exists");
        
        accountEntity.setBalance(BigDecimal.ZERO);
        
        return accountRepository.save(accountEntity);
    }

    public AccountEntity login(AccountEntity accountEntity) {
        Optional<AccountEntity> foundAccount = accountRepository.findByEmail(accountEntity.getEmail());
        
        if(foundAccount.isEmpty())
            throw new AccountNotFoundException("Account not found");
        
        AccountEntity loggedAccount = foundAccount.get();
        
        if(!loggedAccount.getPassword().equals(accountEntity.getPassword()))
            throw new WrongPasswordException("Wrong Password");
        
        return loggedAccount;
    }

    public AccountEntity deposit(Long id, BigDecimal amount) {
        Optional<AccountEntity> foundAccount = accountRepository.findById(id);
        if(foundAccount.isEmpty())
            throw new AccountNotFoundException("Account not found");

        if(amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new WrongDepositException("Wrong Deposit");

        AccountEntity updatedAccount = foundAccount.get();
        updatedAccount.deposit(amount);
        return accountRepository.save(updatedAccount);
    }
}
