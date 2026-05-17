package com.janwypych.bankApiApplication.services;

import com.janwypych.bankApiApplication.entities.AccountEntity;
import com.janwypych.bankApiApplication.entities.TransactionEntity;
import com.janwypych.bankApiApplication.entities.enums.TransactionTypeEnum;
import com.janwypych.bankApiApplication.exeption.*;
import com.janwypych.bankApiApplication.repositories.AccountRepository;
import com.janwypych.bankApiApplication.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
    this.accountRepository = accountRepository;
    this.transactionRepository = transactionRepository; }

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

    @Transactional
    public AccountEntity deposit(Long id, BigDecimal amount) {
        Optional<AccountEntity> foundAccount = accountRepository.findById(id);

        if(foundAccount.isEmpty())
            throw new AccountNotFoundException("Account not found");

        AccountEntity updatedAccount = foundAccount.get();
        updatedAccount.deposit(amount);

        transactionRepository.save(TransactionEntity
                .builder()
                .amount(amount)
                .type(TransactionTypeEnum.DEPOSIT)
                .time(LocalDateTime.now())
                .senderAccount(updatedAccount)
                .receiverId(null)
                .build());

        return accountRepository.save(updatedAccount);
    }

    @Transactional
    public AccountEntity withdraw(Long id, BigDecimal amount) {
        Optional<AccountEntity> foundAccount = accountRepository.findById(id);

        if(foundAccount.isEmpty())
            throw new AccountNotFoundException("Account not found");

        AccountEntity updatedAccount = foundAccount.get();

        if(amount.compareTo(updatedAccount.getBalance()) > 0)
            throw new WrongWithdrawException("Not enough balance");

        updatedAccount.withdraw(amount);

        transactionRepository.save(TransactionEntity
                .builder()
                .amount(amount)
                .type(TransactionTypeEnum.WITHDRAW)
                .time(LocalDateTime.now())
                .senderAccount(updatedAccount)
                .receiverId(null)
                .build());

        return accountRepository.save(updatedAccount);
    }

    @Transactional
    public AccountEntity transfer(Long senderId, Long receiverId, BigDecimal amount) {
        if(Objects.equals(senderId, receiverId))
            throw new InvalidIdException("Sender and Receiver Id cannot be the same");

        Optional<AccountEntity> optionalSenderAccount = accountRepository.findById(senderId);

        if(optionalSenderAccount.isEmpty())
            throw new AccountNotFoundException("Sender account not found");

        Optional<AccountEntity> optionalReceiverAccount = accountRepository.findById(receiverId);

        if(optionalReceiverAccount.isEmpty())
            throw new AccountNotFoundException("Receiver account not found");

        AccountEntity senderAccount = optionalSenderAccount.get();

        if(amount.compareTo(senderAccount.getBalance()) > 0)
            throw new WrongTransferException("Not enough balance");

        AccountEntity receiverAccount = optionalReceiverAccount.get();

        senderAccount.withdraw(amount);
        receiverAccount.deposit(amount);
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        transactionRepository.save(TransactionEntity
                .builder()
                .amount(amount)
                .type(TransactionTypeEnum.TRANSFER)
                .time(LocalDateTime.now())
                .senderAccount(senderAccount)
                .receiverId(receiverAccount.getId())
                .build());

        return senderAccount;
    }
}
