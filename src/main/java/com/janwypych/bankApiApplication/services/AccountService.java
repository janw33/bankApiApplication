package com.janwypych.bankApiApplication.services;

import com.janwypych.bankApiApplication.Dto.ChangeStatusRequest;
import com.janwypych.bankApiApplication.entities.AccountEntity;
import com.janwypych.bankApiApplication.entities.TransactionEntity;
import com.janwypych.bankApiApplication.entities.enums.AccountStatus;
import com.janwypych.bankApiApplication.entities.enums.TransactionTypeEnum;
import com.janwypych.bankApiApplication.exception.*;
import com.janwypych.bankApiApplication.repositories.AccountRepository;
import com.janwypych.bankApiApplication.repositories.TransactionRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository, PasswordEncoder passwordEncoder) {
    this.accountRepository = accountRepository;
    this.transactionRepository = transactionRepository;
    this.passwordEncoder = passwordEncoder;}

    public AccountEntity addAccount(AccountEntity accountEntity) {
        if(accountRepository.existsByEmail(accountEntity.getEmail()))
            throw new EmailAlreadyExistsException("Email already exists");
        
        accountEntity.setBalance(BigDecimal.ZERO);
        accountEntity.setStatus(AccountStatus.ACTIVE);
        accountEntity.setPassword(
                passwordEncoder.encode(accountEntity.getPassword())
        );

        return accountRepository.save(accountEntity);
    }

    public AccountEntity login(AccountEntity accountEntity) {
        Optional<AccountEntity> foundAccount = accountRepository.findByEmail(accountEntity.getEmail());
        
        if(foundAccount.isEmpty())
            throw new AccountNotFoundException("Account not found");
        
        AccountEntity loggedAccount = foundAccount.get();

        if(loggedAccount.getStatus() == AccountStatus.INACTIVE)
            throw new AccountIsInactiveException("Account is inactive");

        if(!passwordEncoder.matches(accountEntity.getPassword(), loggedAccount.getPassword()))
            throw new WrongPasswordException("Wrong Password");
        
        return loggedAccount;
    }

    @Transactional
    public AccountEntity deposit(Long id, BigDecimal amount) {
        Optional<AccountEntity> foundAccount = accountRepository.findById(id);

        if(foundAccount.isEmpty())
            throw new AccountNotFoundException("Account not found");

        AccountEntity updatedAccount = foundAccount.get();

        if(updatedAccount.getStatus() == AccountStatus.INACTIVE)
            throw new AccountIsInactiveException("Account is inactive");

        updatedAccount.deposit(amount);

        transactionRepository.save(TransactionEntity
                .builder()
                .amount(amount)
                .type(TransactionTypeEnum.DEPOSIT)
                .time(LocalDateTime.now())
                .senderAccount(updatedAccount)
                .receiverAccount(null)
                .build());

        return accountRepository.save(updatedAccount);
    }

    @Transactional
    public AccountEntity withdraw(Long id, BigDecimal amount) {
        Optional<AccountEntity> foundAccount = accountRepository.findById(id);

        if(foundAccount.isEmpty())
            throw new AccountNotFoundException("Account not found");

        AccountEntity updatedAccount = foundAccount.get();

        if(updatedAccount.getStatus() == AccountStatus.INACTIVE)
            throw new AccountIsInactiveException("Account is inactive");

        if(amount.compareTo(updatedAccount.getBalance()) > 0)
            throw new WrongWithdrawException("Not enough balance");

        updatedAccount.withdraw(amount);

        transactionRepository.save(TransactionEntity
                .builder()
                .amount(amount)
                .type(TransactionTypeEnum.WITHDRAW)
                .time(LocalDateTime.now())
                .senderAccount(updatedAccount)
                .receiverAccount(null)
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

        AccountEntity senderAccount = optionalSenderAccount.get();

        if(senderAccount.getStatus() == AccountStatus.INACTIVE)
            throw new AccountIsInactiveException("Sender Account is inactive");

        Optional<AccountEntity> optionalReceiverAccount = accountRepository.findById(receiverId);

        if(optionalReceiverAccount.isEmpty())
            throw new AccountNotFoundException("Receiver account not found");

        AccountEntity receiverAccount = optionalReceiverAccount.get();

        if(receiverAccount.getStatus() == AccountStatus.INACTIVE)
            throw new AccountIsInactiveException("Receiver Account is inactive");

        if(amount.compareTo(senderAccount.getBalance()) > 0)
            throw new WrongTransferException("Not enough balance");

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
                .receiverAccount(receiverAccount)
                .build());

        return senderAccount;
    }


    public AccountEntity changeStatus(Long id, AccountStatus status) {
        Optional<AccountEntity> optionalFoundAccount = accountRepository.findById(id);

        if(optionalFoundAccount.isEmpty())
            throw new AccountNotFoundException("Account not found");

        AccountEntity foundAccount = optionalFoundAccount.get();
        foundAccount.setStatus(status);
        return accountRepository.save(foundAccount);
    }
}

