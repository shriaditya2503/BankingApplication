package com.project.BankingApplication.controller;

import com.project.BankingApplication.entity.Account;
import com.project.BankingApplication.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/fetch-account/{id}")
    public Account fetchAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }
}
