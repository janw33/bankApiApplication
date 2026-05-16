package com.janwypych.bankApiApplication.repositories;

import com.janwypych.bankApiApplication.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findAllBySenderAccountId(Long id);
}
