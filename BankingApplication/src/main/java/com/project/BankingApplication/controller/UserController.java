package com.project.BankingApplication.controller;

import com.project.BankingApplication.dto.*;
import com.project.BankingApplication.entity.User;
import com.project.BankingApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto dto) {
        return userService.login(dto);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserInfoDto userInfoDto) {
        return userService.registerUser(userInfoDto);
    }

    @PostMapping("/check-balance")
    public ResponseEntity<BigDecimal> checkBalance(){
        return userService.getBalance();
    }

    @GetMapping("/get-name")
    public ResponseEntity<String> getName() {
        return userService.getName();
    }

    @GetMapping("/fetch-user-details")
    public ResponseEntity<User> fetchUserDetails() {
        return userService.fetchUserDetails();
    }

    @PostMapping("/update-user-details")
    public ResponseEntity<String> updateUserDetails(@RequestBody UpdateUserDetailsDto updateUserDetailsDto) {
        return userService.updateUserDetails(updateUserDetailsDto);
    }

    @PostMapping("/credit")
    public ResponseEntity<String> creditAmount(@RequestBody CreditDebitDto creditDebitDto) {
        return userService.creditAmount(creditDebitDto);
    }

    @PostMapping("/debit")
    public ResponseEntity<String> debitAmount(@RequestBody CreditDebitDto creditDebitDto) {
        return userService.debitAmount(creditDebitDto);
    }

    @PostMapping("/transfer-fund")
    public ResponseEntity<String> transferFund(@RequestBody TransferFundDto transferFundDto) {
        return userService.transferFund(transferFundDto);
    }

    @GetMapping("/updates")
    public String updates() {
        return "Success";
    }
}

