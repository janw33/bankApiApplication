package com.janwypych.bankApiApplication.controllers;

import com.janwypych.bankApiApplication.Dto.CreateAccountRequest;
import com.janwypych.bankApiApplication.Dto.LoginRequest;
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
                MockMvcResultMatchers.jsonPath("$.firstName").value("Jan")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value("Wypych")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("janWypych@email.com")
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
}
