package com.studygroup.studygroup.dto;

import com.studygroup.studygroup.Enums.RequestStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreatorRequestDTO {
     public int RequestId ;

    public String StudentId ;
    public String StudentName ;

    public int GroupId;
    public String GroupSubject ;

    public RequestStatus Status ;
    public java.time.LocalDateTime RequestedAt ;
    }

