package com.janwypych.bankApiApplication.services;

import com.janwypych.bankApiApplication.entities.TransactionEntity;
import com.janwypych.bankApiApplication.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionEntity> findAccountTransactions(Long id) {
        return transactionRepository.findAllBySenderAccountId(id);
    }
}
