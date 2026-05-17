package com.janwypych.bankApiApplication.controllers;

import com.janwypych.bankApiApplication.Dto.ChangeStatusRequest;
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
public class ChangeStatusControllerIntegrationTests {
    private final AccountService accountService;
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @Autowired
    public ChangeStatusControllerIntegrationTests(MockMvc mockMvc, AccountService accountService) {
        this.objectMapper = new ObjectMapper();
        this.mockMvc = mockMvc;
        this.accountService = accountService;
    }

    @Test
    public void testThatChangeStatusReturnHttp400WhenIdIsNull() throws Exception {
        ChangeStatusRequest changeStatusRequest = TestDataUtil.createChangeStatusRequest();
        String statusJson = objectMapper.writeValueAsString(changeStatusRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/accounts/null/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
    @Test
    public void testThatChangeStatusReturnHttp400WhenStatusIsNull() throws Exception {
        ChangeStatusRequest changeStatusRequest = TestDataUtil.createChangeStatusRequest();
        changeStatusRequest.setStatus(null);
        String statusJson = objectMapper.writeValueAsString(changeStatusRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/accounts/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
    @Test
    public void testThatChangeStatusReturnHttp404WhenAccountDoesNotExist() throws Exception {
        ChangeStatusRequest changeStatusRequest = TestDataUtil.createChangeStatusRequest();
        String statusJson = objectMapper.writeValueAsString(changeStatusRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/accounts/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatChangeStatusReturnHttp200WhenAccountExistAndStatusIsValid() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity1();
        accountService.addAccount(accountEntity);

        ChangeStatusRequest changeStatusRequest = TestDataUtil.createChangeStatusRequest();
        String statusJson = objectMapper.writeValueAsString(changeStatusRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/accounts/" + accountEntity.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatChangeStatusReturnAccountWhenAccountExistAndStatusIsValid() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity1();
        accountService.addAccount(accountEntity);

        ChangeStatusRequest changeStatusRequest = TestDataUtil.createChangeStatusRequest();
        String statusJson = objectMapper.writeValueAsString(changeStatusRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/accounts/" + accountEntity.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value(accountEntity.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value(accountEntity.getLastName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(accountEntity.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.balance").value(accountEntity.getBalance().intValue())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").value(changeStatusRequest.getStatus().toString())
        );
    }
}
