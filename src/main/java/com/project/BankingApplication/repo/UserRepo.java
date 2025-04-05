package com.project.BankingApplication.repo;

import com.project.BankingApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByAccountNum(String accountNum);

    User findByEmail(String email);

    User findByAccountNum(String accountNum);

}
