package com.janwypych.bankApiApplication.mappers;

import com.janwypych.bankApiApplication.Dto.AccountResponse;
import com.janwypych.bankApiApplication.Dto.TransactionDto;
import com.janwypych.bankApiApplication.entities.AccountEntity;
import com.janwypych.bankApiApplication.entities.TransactionEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    private final ModelMapper modelMapper;

    public TransactionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TransactionDto mapToTransactionDto(TransactionEntity transactionEntity) {
        return modelMapper.map(transactionEntity, TransactionDto.class);
    }

    public TransactionEntity mapFromTransactionDto(TransactionDto transactionDto) {
        return modelMapper.map(transactionDto, TransactionEntity.class);
    }
}
