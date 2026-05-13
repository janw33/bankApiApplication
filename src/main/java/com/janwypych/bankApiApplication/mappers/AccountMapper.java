package com.janwypych.bankApiApplication.mappers;

import com.janwypych.bankApiApplication.Dto.AccountResponse;
import com.janwypych.bankApiApplication.Dto.CreateAccountRequest;
import com.janwypych.bankApiApplication.entities.AccountEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

    @Component
    public class AccountMapper {
        private final ModelMapper modelMapper;

        public AccountMapper(ModelMapper modelMapper) {
            this.modelMapper = modelMapper;
        }

        public AccountResponse mapToAccountResponse(AccountEntity accountEntity) {
            return modelMapper.map(accountEntity, AccountResponse.class);
        }

        public AccountEntity mapFromAccountResponse(AccountResponse accountResponse) {
            return modelMapper.map(accountResponse, AccountEntity.class);
        }

        public AccountEntity mapFromCreateAccountRequest(CreateAccountRequest accountRequest) {
            return modelMapper.map(accountRequest, AccountEntity.class);
        }
    }

