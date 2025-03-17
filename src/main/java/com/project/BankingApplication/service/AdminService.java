package com.project.BankingApplication.service;

import com.project.BankingApplication.repo.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    AdminRepo adminRepo;

}
