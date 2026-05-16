package com.janwypych.bankApiApplication.controllers;

import com.janwypych.bankApiApplication.Dto.TransactionDto;
import com.janwypych.bankApiApplication.entities.TransactionEntity;
import com.janwypych.bankApiApplication.mappers.TransactionMapper;
import com.janwypych.bankApiApplication.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionController(TransactionService transactionService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @GetMapping("/transactions/account/{id}")
    public Page<TransactionDto> getAccountTransactions(
            @PathVariable("id") Long id,
            Pageable pageable) {

        Page<TransactionEntity> transactionsEntities =
                transactionService.findAllAccountTransactions(id, pageable);

        return transactionsEntities.map(transactionMapper::mapToTransactionDto);
    }
}
