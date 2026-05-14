package com.janwypych.bankApiApplication.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "accounts")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; //add hash later

    @Column(nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "account")
    private List<TransactionEntity> transactions;


    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
    public void withdraw(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }
}
