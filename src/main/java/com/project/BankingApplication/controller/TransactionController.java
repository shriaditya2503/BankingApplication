package com.project.BankingApplication.controller;

import com.project.BankingApplication.dto.ApiResponseDto;
import com.project.BankingApplication.dto.TransferFundDto;
import com.project.BankingApplication.service.AccountService;
import com.project.BankingApplication.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @PostMapping("/fund-transfer")
    public Object fundTransfer(@RequestBody TransferFundDto transferFundDto) {

        if(!accountService.accountExists(transferFundDto.getFromAccount()) || !accountService.accountExists(transferFundDto.getToAccount())) {
            return new ApiResponseDto("Invalid User Id", HttpStatus.BAD_REQUEST.value());
        }
        if(accountService.checkBalance(transferFundDto.getFromAccount()).compareTo(transferFundDto.getAmount()) < 0) {
            return new ApiResponseDto("Insufficient balance", HttpStatus.BAD_REQUEST.value());
        }
        return transactionService.transferFund(transferFundDto.getFromAccount(), transferFundDto.getToAccount(), transferFundDto.getAmount());
    }
}
