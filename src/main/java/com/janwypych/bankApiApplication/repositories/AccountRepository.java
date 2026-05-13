package com.janwypych.bankApiApplication.repositories;

import com.janwypych.bankApiApplication.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}
