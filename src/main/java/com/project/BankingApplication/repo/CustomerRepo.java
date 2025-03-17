package com.project.BankingApplication.repo;

import com.project.BankingApplication.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    boolean existsByUserId(String userId);

    Customer findByUserId(String userId);

}
