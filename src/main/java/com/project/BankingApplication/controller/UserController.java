package com.project.BankingApplication.controller;

import com.project.BankingApplication.dto.*;
import com.project.BankingApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public ApiResponseDto createUser(@RequestBody UserDetailsDto userDetails) {
        return userService.createUser(userDetails);
    }

    @GetMapping("/fetch-user-details")
    public ResponseEntity<?> fetchUserDetails(@RequestBody EnquiryDto enquiryDto) {
        return userService.fetchUserDetails(enquiryDto);
    }

    @PostMapping("/update-user-details")
    public ApiResponseDto updateUserDetails(@RequestParam String email, @RequestBody UpdateUserDetailsDto updateUserDetailsDto) {
        return userService.updateUserDetails(email, updateUserDetailsDto);
    }

    @PostMapping("/credit")
    public ApiResponseDto creditAmount(@RequestBody CreditDebitDto creditDebitDto) {
        return userService.creditAmount(creditDebitDto);
    }

    @PostMapping("/debit")
    public ApiResponseDto debitAmount(@RequestBody CreditDebitDto creditDebitDto) {
        return userService.debitAmount(creditDebitDto);
    }

    @PostMapping("/transfer-fund")
    public ApiResponseDto transferFund(@RequestBody TransferFundDto transferFundDto) {
        return userService.transferFund(transferFundDto);
    }

    @PostMapping("/check-balance")
    public ResponseEntity<?> checkBalance(@RequestBody EnquiryDto enquiryDto){
        return userService.checkBalance(enquiryDto);
    }
}

