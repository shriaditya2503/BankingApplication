package com.project.BankingApplication.service;

import com.project.BankingApplication.entity.Transaction;
import com.project.BankingApplication.enums.TransactionStatus;
import com.project.BankingApplication.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    TransactionRepo transactionRepo;

    @Autowired
    AccountService accountService;

    public Transaction transferFund(Long fromAccount, Long toAccount, BigDecimal amount) {
        accountService.decreaseBalance(fromAccount, amount);
        accountService.increaseBalance(toAccount, amount);

        Transaction transaction = new Transaction();
        transaction.setFromAccount(accountService.getAccount(fromAccount));
        transaction.setToAccount(accountService.getAccount(toAccount));
        transaction.setAmount(amount);
        transaction.setStatus(TransactionStatus.SUCCESSFUL);
        transaction.setTimeStamp(LocalDateTime.now());

        return transactionRepo.save(transaction);
    }

}
