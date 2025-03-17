package com.project.BankingApplication.repo;

import com.project.BankingApplication.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    boolean existsByAccountNum(String accountNum);

}
