package com.studygroup.studygroup.Models;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.studygroup.studygroup.Enums.Role;

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