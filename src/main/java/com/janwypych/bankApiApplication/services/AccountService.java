package com.janwypych.bankApiApplication.services;

import com.janwypych.bankApiApplication.entities.AccountEntity;
import com.janwypych.bankApiApplication.exeption.*;
import com.janwypych.bankApiApplication.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new WrongDepositException("Wrong deposit");

        AccountEntity updatedAccount = foundAccount.get();
        updatedAccount.deposit(amount);
        return accountRepository.save(updatedAccount);
    }

    public AccountEntity withdraw(Long id, BigDecimal amount) {
        Optional<AccountEntity> foundAccount = accountRepository.findById(id);
        if(foundAccount.isEmpty())
            throw new AccountNotFoundException("Account not found");

        AccountEntity updatedAccount = foundAccount.get();

        if(amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(updatedAccount.getBalance()) > 0)
            throw new WrongWithdrawException("Wrong withdraw");

        updatedAccount.withdraw(amount);
        return accountRepository.save(updatedAccount);
    }

    @Transactional
    public AccountEntity transfer(Long senderId, Long receiverId, BigDecimal amount) {
        Optional<AccountEntity> optionalSenderAccount = accountRepository.findById(senderId);

        if(amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new WrongTransferAmountException("Amount is invalid");

        if(optionalSenderAccount.isEmpty())
            throw new AccountNotFoundException("Sender account not found");

        Optional<AccountEntity> optionalReceiverAccount = accountRepository.findById(receiverId);
        if(optionalReceiverAccount.isEmpty())
            throw new AccountNotFoundException("Receiver account not found");

        AccountEntity senderAccount = optionalSenderAccount.get();
        AccountEntity receiverAccount = optionalReceiverAccount.get();

        if(amount.compareTo(senderAccount.getBalance()) > 0)
            throw new WrongTransferAmountException("Amount is invalid");

        senderAccount.withdraw(amount);
        receiverAccount.deposit(amount);
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);
        return senderAccount;
    }
}
