package com.janwypych.bankApiApplication.controllers;

import com.janwypych.bankApiApplication.Dto.CreateAccountRequest;
import com.janwypych.bankApiApplication.Dto.DepositRequest;
import com.janwypych.bankApiApplication.Dto.LoginRequest;
import com.janwypych.bankApiApplication.Dto.WithdrawRequest;
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
public class AccountControllerIntegrationTests {
    private final AccountService accountService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public AccountControllerIntegrationTests(AccountService accountService, MockMvc mockMvc) {
        this.accountService = accountService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateAccountReturnsHttp201WhenEmailAvailable() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }
    @Test
    public void testThatCreateAccountReturnsHttp409WhenEmailUnavailable() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity();
        accountService.addAccount(accountEntity);

        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        );
    }
    @Test
    public void testThatCreateAccountReturnsAccount() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value(createAccountRequest.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value(createAccountRequest.getLastName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(createAccountRequest.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.balance").value(BigDecimal.ZERO)
        );
    }
    @Test
    public void testThatLoginReturnsHttp404WhenAccountDoesntExist() throws Exception {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }
    @Test
    public void testThatLoginReturnsHttp401WhenGivenPasswordIsWrong() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity();
        accountService.addAccount(accountEntity);

        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        loginRequest.setPassword("???");
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isUnauthorized()
        );
    }
    @Test
    public void testThatLoginReturnsHttp200WhenAccountExistAndPasswordIsValid() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity();
        accountService.addAccount(accountEntity);

        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }
    @Test
    public void testThatLoginReturnsAccount() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity();
        accountService.addAccount(accountEntity);

        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value(accountEntity.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value(accountEntity.getLastName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(accountEntity.getEmail())
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
    public void testThatDepositReturnsHttp400WhenAccountExistButAmountIsLowerThan0() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity();
        accountService.addAccount(accountEntity);

        DepositRequest depositRequest = TestDataUtil.createDepositRequest();
        depositRequest.setAmount(BigDecimal.valueOf(-1));
        String depositJson = objectMapper.writeValueAsString(depositRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/deposit/" + accountEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
    @Test
    public void testThatDepositReturnsHttp200WhenAccountExistAndAmountIsValid() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity();
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
        AccountEntity accountEntity = TestDataUtil.createAccountEntity();
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
    public void testThatWithdrawReturnsHttp400WhenAccountExistButAmountIsLowerThanZero() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity();
        accountService.addAccount(accountEntity);

        WithdrawRequest withdrawRequest = TestDataUtil.createWithdrawRequest();
        withdrawRequest.setAmount(BigDecimal.valueOf(-1));
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
    public void testThatWithdrawReturnsHttp400WhenAccountExistButAmountIsGreaterThanBalance() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity();
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
        AccountEntity accountEntity = TestDataUtil.createAccountEntity();
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
        AccountEntity accountEntity = TestDataUtil.createAccountEntity();
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
