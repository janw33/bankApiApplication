package com.janwypych.bankApiApplication.controllers;

import com.janwypych.bankApiApplication.Dto.CreateAccountRequest;
import com.janwypych.bankApiApplication.TestDataUtil;
import com.janwypych.bankApiApplication.entities.AccountEntity;
import com.janwypych.bankApiApplication.entities.enums.AccountStatus;
import com.janwypych.bankApiApplication.repositories.AccountRepository;
import com.janwypych.bankApiApplication.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class CreateAccountControllerIntegrationTests {
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CreateAccountControllerIntegrationTests(AccountService accountService, MockMvc mockMvc, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    public void testThatCreateAccountReturnsHttp400WhenFirstNameIsBlank() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        createAccountRequest.setFirstName("   ");
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
    @Test
    public void testThatCreateAccountReturnsHttp400WhenFirstNameIsTooShort() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        createAccountRequest.setFirstName("a");
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
    @Test
    public void testThatCreateAccountReturnsHttp400WhenFirstNameIsTooLong() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        createAccountRequest.setFirstName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
    @Test
    public void testThatCreateAccountReturnsHttp400WhenFirstNameContainsSpecialCharacters() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        createAccountRequest.setFirstName("Jan123");
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
    @Test
    public void testThatCreateAccountReturnsHttp400WhenLastNameIsBlank() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        createAccountRequest.setLastName("   ");
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
    @Test
    public void testThatCreateAccountReturnsHttp400WhenLastNameIsTooShort() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        createAccountRequest.setLastName("a");
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
    @Test
    public void testThatCreateAccountReturnsHttp400WhenLastNameIsTooLong() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        createAccountRequest.setLastName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
    @Test
    public void testThatCreateAccountReturnsHttp400WhenLastNameContainsSpecialCharacters() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        createAccountRequest.setLastName("Jan123");
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
    @Test
    public void testThatCreateAccountReturnsHttp400WhenEmailIsBlank() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        createAccountRequest.setEmail("   ");
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateAccountReturnsHttp400WhenEmailIsTooLong() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        createAccountRequest.setEmail("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@email.com");
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
    @Test
    public void testThatCreateAccountReturnsHttp400WhenEmailIsBadFormat() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        createAccountRequest.setEmail("Jan123");
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
    @Test
    public void testThatCreateAccountReturnsHttp400WhenPasswordIsBlank() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        createAccountRequest.setPassword("   ");
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateAccountReturnsHttp400WhenPasswordIsTooLong() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        createAccountRequest.setPassword("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateAccountReturnsHttp409WhenEmailUnavailable() throws Exception {
        AccountEntity accountEntity = TestDataUtil.createAccountEntity1();
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
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").value(AccountStatus.ACTIVE.toString())
        );
    }
    @Test
    public void testThatCreateAccountStoresHashedPassword() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createCreateAccountRequest();
        String accountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );;

        Optional<AccountEntity> foundAccount = accountRepository.findByEmail(createAccountRequest.getEmail());
        AccountEntity savedAccount = foundAccount.get();
        assertNotEquals(createAccountRequest.getPassword(), savedAccount.getPassword());

        assertTrue(
                passwordEncoder.matches(
                        createAccountRequest.getPassword(),
                        savedAccount.getPassword()
                )
        );
    }
}
