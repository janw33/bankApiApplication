package com.janwypych.bankApiApplication.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal balance;
}
