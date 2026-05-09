package com.studygroup.studygroup.dto;

import lombok.Data;

@Data
public class GroupDTO {

    private Integer id;
    private String subject;
    private String description;
    private String meetingType;
    private String location;
    private java.time.LocalDateTime meetingTime;
    private Integer maxMembers;
    private String imageUrl;
    private String creatorName;
}