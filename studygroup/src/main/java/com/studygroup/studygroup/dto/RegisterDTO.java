package com.studygroup.studygroup.dto;
import jakarta.validation.constraints.*;

import org.antlr.v4.runtime.misc.NotNull;

import com.studygroup.studygroup.Enums.Role;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Name is required")
    private String name;

    @jakarta.validation.constraints.NotNull(message = "Role is required (STUDENT, CREATOR, ADMIN)")
    private Role role;
}