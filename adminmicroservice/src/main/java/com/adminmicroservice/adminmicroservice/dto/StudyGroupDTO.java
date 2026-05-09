package com.adminmicroservice.adminmicroservice.dto;

import com.adminmicroservice.adminmicroservice.Enums.GroupStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyGroupDTO {
   public int Id ;
    public String Subject ;
    public String Description ;

    public int MaxMembers ;
    public String CreatorName ;

     public String MeetingType ;

    public String Location  = "Online";
    public GroupStatus Status ;
}
