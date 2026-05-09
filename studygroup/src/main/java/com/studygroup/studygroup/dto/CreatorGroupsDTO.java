package com.studygroup.studygroup.dto;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class CreatorGroupsDTO {
    private Integer id;            
    private String subject;
    private String description;
    private int maxMembers;
    private java.time.LocalDateTime meetingTime;
    private String meetingType;
    private String location;
}
