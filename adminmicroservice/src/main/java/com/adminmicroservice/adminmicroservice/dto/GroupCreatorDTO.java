package com.adminmicroservice.adminmicroservice.dto;

import com.adminmicroservice.adminmicroservice.Enums.CreatorStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupCreatorDTO {
    public String UserId ;
    public String Name ;
    public String Email ;
    public CreatorStatus Status ;
}
