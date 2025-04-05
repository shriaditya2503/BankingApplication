package com.project.BankingApplication.service;

import com.project.BankingApplication.dto.*;
import com.project.BankingApplication.entity.Transaction;
import com.project.BankingApplication.entity.User;
import com.project.BankingApplication.enums.Role;
import com.project.BankingApplication.enums.TransactionType;
import com.project.BankingApplication.repo.TransactionRepo;
import com.project.BankingApplication.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    TransactionRepo transactionRepo;

    // create new user
    @Transactional
    public ApiResponseDto createUser(UserDetailsDto userDetails) {
        if(userRepo.existsByEmail(userDetails.getEmail())) {
            return ApiResponseDto.builder()
                    .statusCode(HttpStatus.CONFLICT.value())
                    .message("Email not available")
                    .build();
        }

        User newUser = User.builder()
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .phoneNum(userDetails.getPhoneNum())
                .email(userDetails.getEmail())
                .password(userDetails.getPassword())
                .accountNum(generateAccountNum())
                .balance(BigDecimal.ZERO)
                .status("ACTIVE")
                .role(Role.ROLE_ADMIN)
                .creationDate(LocalDateTime.now())
                .modificationDate(null)
                .build();

        User savedUser = userRepo.save(newUser);

        return ApiResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account Created Successfully")
                .build();
    }

    // fetch user details
    public ResponseEntity<?> fetchUserDetails(EnquiryDto enquiryDto) {
        if(!existsByEmail(enquiryDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponseDto.builder()
                            .statusCode(HttpStatus.CONFLICT.value())
                            .message("Invalid Email")
                            .build());
        }

        User user = userRepo.findByEmail(enquiryDto.getEmail());

        return ResponseEntity.status(HttpStatus.OK)
                .body(user);
    }

    // update user details
    @Transactional
    public ApiResponseDto updateUserDetails(UpdateUserDetailsDto updateUserDetailsDto) {
        if(!existsByEmail(updateUserDetailsDto.getEmail())) {
            return ApiResponseDto.builder()
                    .statusCode(HttpStatus.CONFLICT.value())
                    .message("Invalid Email")
                    .build();
        }

        User user = getUserByEmail(updateUserDetailsDto.getEmail());

        user.setPassword(updateUserDetailsDto.getPassword());

        user.setPhoneNum(updateUserDetailsDto.getPhoneNum());

        user.setModificationDate(LocalDateTime.now());

        return ApiResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("User Details Updated")
                .build();
    }

    // credit amount
    @Transactional
    public ApiResponseDto creditAmount(CreditDebitDto creditDebitDto) {
        if(!existsByAccountNum(creditDebitDto.getAccountNum())) {
            return ApiResponseDto.builder()
                    .statusCode(HttpStatus.CONFLICT.value())
                    .message("Account not found")
                    .build();
        }

        User user = getUserByAccountNum(creditDebitDto.getAccountNum());

        user.setBalance(user.getBalance().add(creditDebitDto.getAmount()));

        Transaction transaction = Transaction.builder()
                .accountNum(creditDebitDto.getAccountNum())
                .amount(creditDebitDto.getAmount())
                .transactionType(TransactionType.CREDIT)
                .timeStamp(LocalDateTime.now())
                .build();

        transactionRepo.save(transaction);

        return ApiResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Rs. " + creditDebitDto.getAmount() + " are credited to your account")
                .build();
    }

    // debit amount
    @Transactional
    public ApiResponseDto debitAmount(CreditDebitDto creditDebitDto) {
        if(!existsByAccountNum(creditDebitDto.getAccountNum())) {
            return ApiResponseDto.builder()
                    .statusCode(HttpStatus.CONFLICT.value())
                    .message("Account not found")
                    .build();
        }

        User user = getUserByAccountNum(creditDebitDto.getAccountNum());

        if(user.getBalance().compareTo(creditDebitDto.getAmount()) < 0) {
            return ApiResponseDto.builder()
                    .statusCode(HttpStatus.CONTINUE.value())
                    .message("Insufficient balance")
                    .build();
        }

        user.setBalance(user.getBalance().subtract(creditDebitDto.getAmount()));

        Transaction transaction = Transaction.builder()
                .accountNum(creditDebitDto.getAccountNum())
                .amount(creditDebitDto.getAmount())
                .transactionType(TransactionType.DEBIT)
                .timeStamp(LocalDateTime.now())
                .build();

        transactionRepo.save(transaction);

        return ApiResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Rs. " + creditDebitDto.getAmount() + " are debited from your account")
                .build();
    }

    // transfer fund
    @Transactional
    public ApiResponseDto transferFund(TransferFundDto transferFundDto) {
        if(!existsByAccountNum(transferFundDto.getFromAccount()) || !existsByAccountNum(transferFundDto.getToAccount())) {
            return ApiResponseDto.builder()
                    .statusCode(HttpStatus.CONFLICT.value())
                    .message("Account not found")
                    .build();
        }

        User fromAccount = getUserByAccountNum(transferFundDto.getFromAccount());
        User toAccount = getUserByAccountNum(transferFundDto.getToAccount());

        if(fromAccount.getBalance().compareTo(transferFundDto.getAmount()) < 0) {
            return ApiResponseDto.builder()
                    .statusCode(HttpStatus.CONTINUE.value())
                    .message("Insufficient balance")
                    .build();
        }

        // update from account balance
        fromAccount.setBalance(fromAccount.getBalance().subtract(transferFundDto.getAmount()));

        Transaction fromTransaction = Transaction.builder()
                .accountNum(transferFundDto.getFromAccount())
                .amount(transferFundDto.getAmount())
                .transactionType(TransactionType.DEBIT)
                .timeStamp(LocalDateTime.now())
                .build();

        transactionRepo.save(fromTransaction);

        // update to account balance
        toAccount.setBalance(fromAccount.getBalance().add(transferFundDto.getAmount()));

        Transaction toTransaction = Transaction.builder()
                .accountNum(transferFundDto.getToAccount())
                .amount(transferFundDto.getAmount())
                .transactionType(TransactionType.CREDIT)
                .timeStamp(LocalDateTime.now())
                .build();

        transactionRepo.save(toTransaction);

        return ApiResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Rs. " + transferFundDto.getAmount() + " are transferred")
                .build();
    }

    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    public boolean existsByAccountNum(String accountNum) {
        return userRepo.existsByAccountNum(accountNum);
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User getUserByAccountNum(String accountNum) {
        return userRepo.findByAccountNum(accountNum);
    }

    public String generateAccountNum() {
        String accountNum;

        do accountNum = generateUniqueAccountNum();

        while (userRepo.existsByAccountNum(accountNum));

        return accountNum;
    }

    private String generateUniqueAccountNum() {
        return String.valueOf(1000000000L + new Random().nextInt(900000000));
    }
}
