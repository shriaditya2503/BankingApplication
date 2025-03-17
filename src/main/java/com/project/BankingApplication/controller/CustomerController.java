package com.project.BankingApplication.controller;

import com.project.BankingApplication.dto.ApiResponseDto;
import com.project.BankingApplication.dto.ChangeCustomerDetailsDto;
import com.project.BankingApplication.entity.Customer;
import com.project.BankingApplication.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping(value = "/create-customer")
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        if(!customerService.existsUserId(customer.getUserId())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiResponseDto("User Id is not available.", HttpStatus.CONFLICT.value()));
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.createCustomer(customer));
    }

    @GetMapping(value = "/{userId}/fetch-customer")
    public Customer fetchCustomer(@PathVariable String userId) {
        return customerService.getCustomer(userId);
    }

    @PostMapping(value = "/{userId}/change-details")
    public Object changeCustomerDetails(@PathVariable String userId, @RequestBody ChangeCustomerDetailsDto changeCustomerDetailsDto) {
        if(!customerService.existsUserId(userId)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiResponseDto("User Id not found", HttpStatus.FORBIDDEN.value()));
        }
        return customerService.changeCustomerDetails(userId, changeCustomerDetailsDto);
    }

//    @PostMapping(value = "/test/{name}")
//    public void test(@PathVariable String name) {
//        System.out.println(name);
//    }
}

