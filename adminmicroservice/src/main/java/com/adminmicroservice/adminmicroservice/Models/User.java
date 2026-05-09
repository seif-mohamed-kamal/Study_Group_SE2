package com.adminmicroservice.adminmicroservice.Models;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.adminmicroservice.adminmicroservice.Enums.Role;


@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    private String id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;
}