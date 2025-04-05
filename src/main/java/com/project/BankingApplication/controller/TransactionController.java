package com.project.BankingApplication.controller;

import com.project.BankingApplication.dto.AccountDto;
import com.project.BankingApplication.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transaction-list")
    public ResponseEntity<?> transactionList(@RequestBody AccountDto accountDto) {
        return transactionService.transactionList(accountDto);
    }
}
