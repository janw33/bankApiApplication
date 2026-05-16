package com.janwypych.bankApiApplication.repositories;

import com.janwypych.bankApiApplication.entities.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    Page<TransactionEntity> findAllBySenderAccountId(Long id, Pageable pageable);
}
