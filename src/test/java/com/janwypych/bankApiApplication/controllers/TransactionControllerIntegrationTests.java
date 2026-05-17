package com.janwypych.bankApiApplication.controllers;

import com.janwypych.bankApiApplication.TestDataUtil;
import com.janwypych.bankApiApplication.entities.AccountEntity;
import com.janwypych.bankApiApplication.entities.enums.TransactionTypeEnum;
import com.janwypych.bankApiApplication.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class TransactionControllerIntegrationTests {
    private final AccountService accountService;
    private final MockMvc mockMvc;
    @Autowired
    public TransactionControllerIntegrationTests(MockMvc mockMvc, AccountService accountService  ) {
        this.accountService = accountService;
        this.mockMvc = mockMvc;
    }

    @Test
    public void testThatGetAccountTransactionReturnHttp400WhenAccountIdIsNull() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/transactions/account/null")
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatGetAccountTransactionReturnHttp404WhenAccountDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/transactions/account/1")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetAccountTransactionReturnHttp200WhenAccountExist() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity1();
        accountService.addAccount(accountEntity);
        accountService.deposit(accountEntity.getId(), BigDecimal.TEN);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/transactions/account/" + accountEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAccountTransactionReturnTransactionWhenAccountExist() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity1();
        accountService.addAccount(accountEntity);
        accountService.deposit(accountEntity.getId(), BigDecimal.TEN);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/transactions/account/" + accountEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].id").value(1L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].amount").value(BigDecimal.TEN.intValue())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].type").value(TransactionTypeEnum.DEPOSIT.toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].senderId").value(accountEntity.getId())
        );
    }
    @Test
    public void testThatGetAccountTransactionReturnTransferTransactionForSenderAndReceiver() throws Exception {
        AccountEntity senderAccount = TestDataUtil.createAccountEntity1();
        accountService.addAccount(senderAccount);
        accountService.deposit(senderAccount.getId(), BigDecimal.TEN);

        AccountEntity receiverAccount = TestDataUtil.createAccountEntity2();
        accountService.addAccount(receiverAccount);

        accountService.transfer(senderAccount.getId(), receiverAccount.getId(), BigDecimal.TEN);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/transactions/account/" + senderAccount.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[1].id").value(2L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[1].amount").value(BigDecimal.TEN.intValue())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[1].type").value(TransactionTypeEnum.TRANSFER.toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[1].senderId").value(senderAccount.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[1].receiverId").value(receiverAccount.getId())
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get("/transactions/account/" + receiverAccount.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].id").value(2L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].amount").value(BigDecimal.TEN.intValue())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].type").value(TransactionTypeEnum.TRANSFER.toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].senderId").value(senderAccount.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].receiverId").value(receiverAccount.getId())
        );
    }
}
