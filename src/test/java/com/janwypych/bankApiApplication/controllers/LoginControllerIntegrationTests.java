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

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class LoginControllerIntegrationTests {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public LoginControllerIntegrationTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatLoginReturnsHttp400WhenEmailIsBlank() throws Exception {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        loginRequest.setEmail("    ");
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatLoginReturnsHttp400WhenEmailIsTooLong() throws Exception {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        loginRequest.setEmail("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@email.com");
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
    @Test
    public void testThatLoginReturnsHttp400WhenEmailIsBadFormat() throws Exception {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        loginRequest.setEmail("Jan123");
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
    @Test
    public void testThatLoginReturnsHttp400WhenPasswordIsBlank() throws Exception {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        loginRequest.setPassword("");
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatLoginReturnsHttp400WhenPasswordIsTooLong() throws Exception {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        loginRequest.setPassword("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
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
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

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
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

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
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value(createAccountRequest.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value(createAccountRequest.getLastName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(createAccountRequest.getEmail())
        );
    }
}
