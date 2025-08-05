package com.project.BankingApplication.entity;

import com.project.BankingApplication.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNum;
    private BigDecimal amount;
    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;
    private LocalDateTime timeStamp;
    private String status;
}

