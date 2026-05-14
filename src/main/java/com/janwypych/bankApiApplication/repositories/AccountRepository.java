package com.janwypych.bankApiApplication.repositories;

import com.janwypych.bankApiApplication.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    boolean existsByEmail(String email);
    Optional<AccountEntity> findByEmail(String email);
}
