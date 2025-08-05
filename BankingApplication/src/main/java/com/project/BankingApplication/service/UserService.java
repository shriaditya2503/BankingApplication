package com.project.BankingApplication.service;

import com.project.BankingApplication.dto.*;
import com.project.BankingApplication.entity.Transaction;
import com.project.BankingApplication.entity.User;
import com.project.BankingApplication.enums.TransactionType;
import com.project.BankingApplication.repo.UserRepo;
import com.project.BankingApplication.security.jwt.JwtService;
import com.project.BankingApplication.utils.EmailBody;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private EmailService emailService;

    // login
    public ResponseEntity<String> login(LoginDto dto) {
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword()));

        if(!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Wrong Credentials");
        }

        String jwtToken = jwtService.generateToken(dto.getEmail());

        System.out.println(jwtToken);

        return ResponseEntity.status(HttpStatus.OK)
                .body(jwtToken);
    }

    // create new user
    @Transactional
    public ResponseEntity<String>  registerUser(UserInfoDto userInfoDto) {
        if(userRepo.existsByEmail(userInfoDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already exists");
        }

        User newUser = User.builder()
                .firstName(userInfoDto.getFirstName())
                .lastName(userInfoDto.getLastName())
                .phoneNum(userInfoDto.getPhoneNum())
                .email(userInfoDto.getEmail())
                .password(userInfoDto.getPassword())
                .accountNum(generateAccountNum())
                .balance(BigDecimal.ZERO)
                .status("ACTIVE")
                .role("USER")
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

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Account created successfully");
    }

    // get name
    public ResponseEntity<String> getName() {

        User user = getCurrentAuthenticatedUser();

        return ResponseEntity.status(HttpStatus.OK)
                .body(user.getFirstName()+" "+user.getLastName());
    }

    // get balance
    public ResponseEntity<BigDecimal> getBalance(){

        User user = getCurrentAuthenticatedUser();

        return ResponseEntity.status(HttpStatus.OK)
                .body(user.getBalance());
    }

    // fetch user details
    public ResponseEntity<User> fetchUserDetails() {

        User user = getCurrentAuthenticatedUser();

        return ResponseEntity.status(HttpStatus.OK)
                .body(user);
    }

    // update user details
    @Transactional
    public ResponseEntity<String> updateUserDetails(UpdateUserDetailsDto updateUserDetailsDto) {

        User user = getCurrentAuthenticatedUser();

        if(updateUserDetailsDto.getEmail() != null) {
            user.setEmail(updateUserDetailsDto.getEmail());
        }

        if(updateUserDetailsDto.getPassword() != null) {
            user.setPassword(updateUserDetailsDto.getPassword());
        }

        if(updateUserDetailsDto.getPhoneNum() != null) {
            user.setPhoneNum(updateUserDetailsDto.getPhoneNum());
        }

        user.setModificationDate(LocalDateTime.now());

        // send email
        EmailDto emailDto = EmailDto.builder()
                .recipientEmail(user.getEmail())
                .subject("Your profile has been successfully updated")
                .text(EmailBody.updateDetails(user))
                .build();
        emailService.sendEmail(emailDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body("User details updated successfully");
    }

    // credit amount
    @Transactional
    public ResponseEntity<String> creditAmount(CreditDebitDto creditDebitDto) {
        if(!userRepo.existsByAccountNum(creditDebitDto.getAccountNum())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Account not found");
        }

        User user = getUserByAccountNum(creditDebitDto.getAccountNum());

        user.setBalance(user.getBalance().add(creditDebitDto.getAmount()));

        Transaction transaction = Transaction.builder()
                .accountNum(user.getAccountNum())
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

        return ResponseEntity.status(HttpStatus.OK)
                .body("Rs. " + creditDebitDto.getAmount() + " are credited to the account");
    }

    // debit amount
    @Transactional
    public ResponseEntity<String> debitAmount(CreditDebitDto creditDebitDto) {
        if(!userRepo.existsByAccountNum(creditDebitDto.getAccountNum())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invalid Email");
        }

        User user = getUserByAccountNum(creditDebitDto.getAccountNum());

        if(user.getBalance().compareTo(creditDebitDto.getAmount()) < 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Insufficient balance");
        }

        user.setBalance(user.getBalance().subtract(creditDebitDto.getAmount()));

        Transaction transaction = Transaction.builder()
                .accountNum(user.getAccountNum())
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

        return ResponseEntity.status(HttpStatus.OK)
                .body("Rs. " + creditDebitDto.getAmount() + " are debited from the account");
    }

    // transfer fund
    @Transactional
    public ResponseEntity<String> transferFund(TransferFundDto transferFundDto) {

        if(!existsByAccountNum(transferFundDto.getToAccount())) {;
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Destination account not found");
        }

        User fromAccount = getCurrentAuthenticatedUser();
        User toAccount = getUserByAccountNum(transferFundDto.getToAccount());

        if(fromAccount.getBalance().compareTo(transferFundDto.getAmount()) < 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Insufficient balance");
        }

        // update fromAccount balance
        fromAccount.setBalance(fromAccount.getBalance().subtract(transferFundDto.getAmount()));

        Transaction fromTransaction = Transaction.builder()
                .accountNum(fromAccount.getAccountNum())
                .amount(transferFundDto.getAmount())
                .transactionType(TransactionType.DEBIT)
                .timeStamp(LocalDateTime.now())
                .build();

        transactionService.saveTransaction(fromTransaction);

        // update toAccount balance
        toAccount.setBalance(toAccount.getBalance().add(transferFundDto.getAmount()));

        Transaction toTransaction = Transaction.builder()
                .accountNum(toAccount.getAccountNum())
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

        return ResponseEntity.status(HttpStatus.OK)
                .body("Rs. " + transferFundDto.getAmount() + " are transferred successfully");

    }

    // utility methods
    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepo.findByEmail(authentication.getName());
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
        do {
            accountNum = generateUniqueAccountNum();
        } while (userRepo.existsByAccountNum(accountNum));
        return accountNum;
    }

    private String generateUniqueAccountNum() {
        return String.valueOf(1000000000L + new Random().nextInt(900000000));
    }
}