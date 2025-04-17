package com.project.BankingApplication.service;

import com.project.BankingApplication.dto.*;
import com.project.BankingApplication.entity.Transaction;
import com.project.BankingApplication.entity.User;
import com.project.BankingApplication.enums.Role;
import com.project.BankingApplication.enums.TransactionType;
import com.project.BankingApplication.repo.UserRepo;
import com.project.BankingApplication.utils.EmailBody;
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
    private UserRepo userRepo;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private EmailService emailService;

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
                .role(Role.ADMIN)
                .creationDate(LocalDateTime.now())
                .modificationDate(null)
                .build();

        User savedUser = userRepo.save(newUser);

        // send email
        EmailDto emailDto = EmailDto.builder()
                .recipientEmail(savedUser.getEmail())
                .subject("Welcome to Lala Finance Bank, "+ savedUser.getFirstName()+" "+savedUser.getLastName() +"!")
                .text(EmailBody.createUserBody(savedUser))
                .build();
        emailService.sendEmail(emailDto);

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
    public ApiResponseDto updateUserDetails(String email, UpdateUserDetailsDto updateUserDetailsDto) {
        if(!existsByEmail(email)) {
            return ApiResponseDto.builder()
                    .statusCode(HttpStatus.CONFLICT.value())
                    .message("Invalid Email")
                    .build();
        }

        User user = getUserByEmail(email);

        user.setEmail(updateUserDetailsDto.getEmail());

        user.setPassword(updateUserDetailsDto.getPassword());

        user.setPhoneNum(updateUserDetailsDto.getPhoneNum());

        user.setModificationDate(LocalDateTime.now());

        // send email
        EmailDto emailDto = EmailDto.builder()
                .recipientEmail(user.getEmail())
                .subject("Your profile has been successfully updated")
                .text(EmailBody.updateDetails(user))
                .build();
        emailService.sendEmail(emailDto);

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

        transactionService.saveTransaction(transaction);

        // send email
        EmailDto emailDto = EmailDto.builder()
                .recipientEmail(user.getEmail())
                .subject("Credit Alert: ₹" + transaction.getAmount() + " credited to your account")
                .text(EmailBody.creditBody(user, transaction, "Self Deposit"))
                .build();
        emailService.sendEmail(emailDto);

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

        transactionService.saveTransaction(transaction);

        // send email
        EmailDto emailDto = EmailDto.builder()
                .recipientEmail(user.getEmail())
                .subject("Debit Alert: ₹" + transaction.getAmount() + " debited from your account")
                .text(EmailBody.debitBody(user, transaction))
                .build();
        emailService.sendEmail(emailDto);

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

        // update fromAccount balance
        fromAccount.setBalance(fromAccount.getBalance().subtract(transferFundDto.getAmount()));

        Transaction fromTransaction = Transaction.builder()
                .accountNum(transferFundDto.getFromAccount())
                .amount(transferFundDto.getAmount())
                .transactionType(TransactionType.DEBIT)
                .timeStamp(LocalDateTime.now())
                .build();

        transactionService.saveTransaction(fromTransaction);

        // update toAccount balance
        toAccount.setBalance(toAccount.getBalance().add(transferFundDto.getAmount()));

        Transaction toTransaction = Transaction.builder()
                .accountNum(transferFundDto.getToAccount())
                .amount(transferFundDto.getAmount())
                .transactionType(TransactionType.CREDIT)
                .timeStamp(LocalDateTime.now())
                .build();

        transactionService.saveTransaction(toTransaction);

        // send from email
        EmailDto debitAlert = EmailDto.builder()
                .recipientEmail(fromAccount.getEmail())
                .subject("Fund Transfer Successful: ₹" + fromTransaction.getAmount() + " sent to " + toAccount.getFirstName()+" "+toAccount.getLastName())
                .text(EmailBody.fundTransferBody(fromAccount, fromTransaction, toAccount.getFirstName()+" "+toAccount.getLastName()))
                .build();
        emailService.sendEmail(debitAlert);

        // send to email
        EmailDto creditAlert = EmailDto.builder()
                .recipientEmail(toAccount.getEmail())
                .subject("Credit Alert: ₹" + toTransaction.getAmount() + " credited to your account")
                .text(EmailBody.creditBody(toAccount, toTransaction, fromAccount.getFirstName()+" "+fromAccount.getLastName()))
                .build();
        emailService.sendEmail(creditAlert);

        return ApiResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Rs. " + transferFundDto.getAmount() + " are transferred")
                .build();
    }

    public ResponseEntity<?> checkBalance(EnquiryDto enquiryDto){
        if(!existsByEmail(enquiryDto.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponseDto.builder()
                            .statusCode(HttpStatus.CONFLICT.value())
                            .message("user not found")
                            .build());
        }
        User user = userRepo.findByEmail(enquiryDto.getEmail());

        return ResponseEntity.status(HttpStatus.OK)
                .body(user.getBalance());
    }

    // utility methods
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