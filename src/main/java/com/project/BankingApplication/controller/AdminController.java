package com.project.BankingApplication.controller;

import com.project.BankingApplication.dto.ApiResponseDto;
import com.project.BankingApplication.entity.Account;
import com.project.BankingApplication.service.AccountService;
import com.project.BankingApplication.service.CustomerService;
import com.project.BankingApplication.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    CustomerService customerService;

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;

    @PostMapping("/withdraw")
    public ApiResponseDto withdraw(@RequestParam Long id, @RequestParam BigDecimal amount) {
        if(!accountService.accountExists(id)) {
            return new ApiResponseDto("Invalid User Id", HttpStatus.BAD_REQUEST.value());
        }
        if(accountService.checkBalance(id).compareTo(amount) < 0) {
            return new ApiResponseDto("Insufficient balance", HttpStatus.BAD_REQUEST.value());
        }
        accountService.decreaseBalance(id, amount);
        return new ApiResponseDto("Rs. " + amount + " Withdrawn", HttpStatus.OK.value());
    }

    @PostMapping("/deposit")
    public ApiResponseDto deposit(@RequestParam(name = "id") Long id, @RequestParam(name = "amount") BigDecimal amount) {

        if(!accountService.accountExists(id)) {
            return new ApiResponseDto("Invalid User Id", HttpStatus.BAD_REQUEST.value());
        }
        accountService.increaseBalance(id, amount);
        return new ApiResponseDto("Rs. " + amount + " Deposited", HttpStatus.OK.value());
    }
}
