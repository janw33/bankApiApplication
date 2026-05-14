package com.janwypych.bankApiApplication.repositories;

import com.janwypych.bankApiApplication.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
