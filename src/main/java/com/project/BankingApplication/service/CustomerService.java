package com.project.BankingApplication.service;

import com.project.BankingApplication.dto.ChangeCustomerDetailsDto;
import com.project.BankingApplication.entity.Account;
import com.project.BankingApplication.entity.Customer;
import com.project.BankingApplication.enums.AccountType;
import com.project.BankingApplication.repo.AccountRepo;
import com.project.BankingApplication.repo.CustomerRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CustomerService {
    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    AccountRepo accountRepo;

    @Transactional
    public Customer createCustomer(Customer customer) {
        String accountNum;
        Account account = new Account();

        do accountNum = generateUniqueAccountNum();
        while (accountRepo.existsByAccountNum(accountNum));

        account.setAccountNum(accountNum);
        account.setAccountType(AccountType.SAVINGS);
        account.setCustomer(customer);
        accountRepo.save(account);

        customer.setAccount(account);
        return customerRepo.save(customer);

    }

    @Transactional
    public Customer changeCustomerDetails(String userId, ChangeCustomerDetailsDto changeCustomerDetailsDto) {
        Customer customer = getCustomer(userId);
        if(changeCustomerDetailsDto.getPassword() != null) {
            customer.setPassword(changeCustomerDetailsDto.getPassword());
        }
        if(changeCustomerDetailsDto.getPhoneNum() != null) {
            customer.setPhoneNum(changeCustomerDetailsDto.getPhoneNum());
        }
        if(changeCustomerDetailsDto.getEmail() != null) {
            customer.setEmail(changeCustomerDetailsDto.getEmail());
        }
        return customer;
    }

    public boolean existsUserId(String userId) {
        return customerRepo.existsByUserId(userId);
    }

    public Customer getCustomer(String userId) {
        return customerRepo.findByUserId(userId);
    }

    private String generateUniqueAccountNum() {
        return String.valueOf(1000000000L + new Random().nextInt(900000000));
    }

}
