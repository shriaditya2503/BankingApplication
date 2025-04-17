package com.project.BankingApplication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.BankingApplication.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNum;
    private String email;
    @JsonIgnore
    private String password;
    private String accountNum;
    private BigDecimal balance;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    private String status;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
}
