package com.janwypych.bankApiApplication.services;

import com.janwypych.bankApiApplication.entities.TransactionEntity;
import com.janwypych.bankApiApplication.exception.AccountNotFoundException;
import com.janwypych.bankApiApplication.repositories.AccountRepository;
import com.janwypych.bankApiApplication.repositories.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Page<TransactionEntity> findAllAccountTransactions(Long id, Pageable pageable) {
        if(!accountRepository.existsById(id))
            throw new AccountNotFoundException("Account does not exist");

        return transactionRepository.findAllBySenderAccountId(id, pageable);
    }
}
