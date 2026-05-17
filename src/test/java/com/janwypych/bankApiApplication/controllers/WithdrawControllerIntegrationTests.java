package com.janwypych.bankApiApplication.controllers;

import com.janwypych.bankApiApplication.Dto.*;
import com.janwypych.bankApiApplication.TestDataUtil;
import com.janwypych.bankApiApplication.entities.AccountEntity;
import com.janwypych.bankApiApplication.entities.enums.AccountStatus;
import com.janwypych.bankApiApplication.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class WithdrawControllerIntegrationTests {
    private final AccountService accountService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public WithdrawControllerIntegrationTests(AccountService accountService, MockMvc mockMvc) {
        this.accountService = accountService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatWithdrawReturnsHttp400WhenIdIsNull() throws Exception {
        WithdrawRequest withdrawRequest = TestDataUtil.createWithdrawRequest();
        String withdrawJson = objectMapper.writeValueAsString(withdrawRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/withdraw/null")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withdrawJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatWithdrawReturnsHttp400WhenAmountIsNull() throws Exception {
        WithdrawRequest withdrawRequest = TestDataUtil.createWithdrawRequest();
        withdrawRequest.setAmount(null);
        String withdrawJson = objectMapper.writeValueAsString(withdrawRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/withdraw/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withdrawJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatWithdrawReturnsHttp400WhenAmountIsLowerThanZero() throws Exception {
        WithdrawRequest withdrawRequest = TestDataUtil.createWithdrawRequest();
        withdrawRequest.setAmount(BigDecimal.valueOf(-1));
        String withdrawJson = objectMapper.writeValueAsString(withdrawRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/withdraw/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withdrawJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatWithdrawReturnsHttp404WhenAccountDoesntExist() throws Exception {
        WithdrawRequest withdrawRequest = TestDataUtil.createWithdrawRequest();
        String withdrawJson = objectMapper.writeValueAsString(withdrawRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/withdraw/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withdrawJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatWithdrawReturnsHttp403WhenAccountIsInactive() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity1();
        accountService.addAccount(accountEntity);
        accountService.changeStatus(accountEntity.getId(), AccountStatus.INACTIVE);

        WithdrawRequest withdrawRequest = TestDataUtil.createWithdrawRequest();
        String withdrawJson = objectMapper.writeValueAsString(withdrawRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/withdraw/" + accountEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withdrawJson)
        ).andExpect(
                MockMvcResultMatchers.status().isForbidden()
        );
    }

    @Test
    public void testThatWithdrawReturnsHttp400WhenAccountExistButAmountIsGreaterThanBalance() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity1();
        accountService.addAccount(accountEntity);

        WithdrawRequest withdrawRequest = TestDataUtil.createWithdrawRequest();
        String withdrawJson = objectMapper.writeValueAsString(withdrawRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/withdraw/" + accountEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withdrawJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatWithdrawReturnsHttp200WhenAccountExistAndAmountIsValid() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity1();
        accountService.addAccount(accountEntity);
        accountService.deposit(accountEntity.getId(), BigDecimal.valueOf(100));

        WithdrawRequest withdrawRequest = TestDataUtil.createWithdrawRequest();
        String withdrawJson = objectMapper.writeValueAsString(withdrawRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/withdraw/" + accountEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withdrawJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatWithdrawReturnsAccountWhenAccountExistAndAmountIsValid() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity1();
        accountService.addAccount(accountEntity);
        AccountEntity updatedAccount = accountService.deposit(accountEntity.getId(), BigDecimal.valueOf(100));

        WithdrawRequest withdrawRequest = TestDataUtil.createWithdrawRequest();
        String withdrawJson = objectMapper.writeValueAsString(withdrawRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/withdraw/" + accountEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withdrawJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value(accountEntity.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value(accountEntity.getLastName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(accountEntity.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.balance").value((updatedAccount.getBalance().subtract(withdrawRequest.getAmount()).intValue())));
    }
}