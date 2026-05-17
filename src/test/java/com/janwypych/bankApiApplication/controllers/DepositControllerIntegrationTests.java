package com.janwypych.bankApiApplication.controllers;

import com.janwypych.bankApiApplication.Dto.*;
import com.janwypych.bankApiApplication.TestDataUtil;
import com.janwypych.bankApiApplication.entities.AccountEntity;
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
public class DepositControllerIntegrationTests {
    private final AccountService accountService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public DepositControllerIntegrationTests(AccountService accountService, MockMvc mockMvc) {
        this.accountService = accountService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatDepositReturnsHttp400WhenIdIsNull() throws Exception {
        DepositRequest depositRequest = TestDataUtil.createDepositRequest();
        String depositJson = objectMapper.writeValueAsString(depositRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/deposit/null")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatDepositReturnsHttp400WhenAmountIsNull() throws Exception {
        DepositRequest depositRequest = TestDataUtil.createDepositRequest();
        depositRequest.setAmount(null);
        String depositJson = objectMapper.writeValueAsString(depositRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/deposit/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatDepositReturnsHttp400WhenAmountIsLessThanOrEqualToZero() throws Exception {
        DepositRequest depositRequest = TestDataUtil.createDepositRequest();
        depositRequest.setAmount(BigDecimal.valueOf(-1));
        String depositJson = objectMapper.writeValueAsString(depositRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/deposit/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatDepositReturnsHttp404WhenAccountDoesntExist() throws Exception {
        DepositRequest depositRequest = TestDataUtil.createDepositRequest();
        String depositJson = objectMapper.writeValueAsString(depositRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/deposit/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatDepositReturnsHttp200WhenAccountExistAndAmountIsValid() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity1();
        accountService.addAccount(accountEntity);

        DepositRequest depositRequest = TestDataUtil.createDepositRequest();
        String depositJson = objectMapper.writeValueAsString(depositRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/deposit/" + accountEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatDepositReturnsAccountWhenAccountExistsAndAmountIsValid() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity1();
        accountService.addAccount(accountEntity);

        DepositRequest depositRequest = TestDataUtil.createDepositRequest();
        String depositJson = objectMapper.writeValueAsString(depositRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/deposit/" + accountEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value(accountEntity.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value(accountEntity.getLastName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(accountEntity.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.balance").value(depositRequest.getAmount().intValue()));
    }
}