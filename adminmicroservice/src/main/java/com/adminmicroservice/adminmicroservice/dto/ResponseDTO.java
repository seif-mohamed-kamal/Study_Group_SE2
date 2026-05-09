package com.adminmicroservice.adminmicroservice.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO<T> {

    private boolean success;
    private String message;
    private T data;
}
