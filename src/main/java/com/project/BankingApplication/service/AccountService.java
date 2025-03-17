package com.project.BankingApplication.service;

import com.project.BankingApplication.entity.Account;
import com.project.BankingApplication.repo.AccountRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    AccountRepo accountRepo;

    @Transactional
    public void increaseBalance(Long id, BigDecimal amount) {
        Account account = getAccount(id);
        account.setBalance(account.getBalance().add(amount));
    }

    @Transactional
    public void decreaseBalance(Long id, BigDecimal amount) {
        Account account = getAccount(id);
        account.setBalance(account.getBalance().subtract(amount));
    }

    public BigDecimal checkBalance(Long id) {
        Account account = getAccount(id);
        return account.getBalance();
    }

    public boolean accountExists(Long id) {
        return accountRepo.existsById(id);
    }

    public Account getAccount(Long id) {
        return accountRepo.findById(id).orElseThrow(()-> new RuntimeException("Account not found"));
     }
}
