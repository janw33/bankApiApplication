package com.janwypych.bankApiApplication.entities;

import com.janwypych.bankApiApplication.entities.enums.TransactionTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionTypeEnum type;

    @Column(nullable = false)
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "sender_account_id", nullable = true)
    private AccountEntity senderAccount;

    @ManyToOne
    @JoinColumn(name = "receiver_account_id", nullable = true)
    private AccountEntity receiverAccount;
}
