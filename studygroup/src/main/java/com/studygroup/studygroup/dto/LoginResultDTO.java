package com.studygroup.studygroup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class LoginResultDTO {
    private String token;
    private String email;
    private String name;
    private List<String> roles;

}