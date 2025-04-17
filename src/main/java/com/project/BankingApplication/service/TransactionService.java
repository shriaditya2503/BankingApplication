package com.project.BankingApplication.service;

import com.project.BankingApplication.dto.AccountDto;
import com.project.BankingApplication.dto.ApiResponseDto;
import com.project.BankingApplication.entity.Transaction;
import com.project.BankingApplication.repo.TransactionRepo;
import com.project.BankingApplication.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private UserRepo userRepo;

    public void saveTransaction(Transaction transaction) {
        transactionRepo.save(transaction);
    }

    public ResponseEntity<?> transactionList(AccountDto accountDto) {
        if(!userRepo.existsByAccountNum(accountDto.getAccountNum())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponseDto.builder()
                            .statusCode(HttpStatus.CONFLICT.value())
                            .message("Account not found")
                            .build());
        }

        List<Transaction> allTransactions = transactionRepo.findAll();

        List<Transaction> transactions = allTransactions.stream()
                .filter(transaction -> transaction.getAccountNum().equals(accountDto.getAccountNum()))
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(transactions);
    }
}
