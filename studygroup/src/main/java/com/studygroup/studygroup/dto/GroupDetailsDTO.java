package com.studygroup.studygroup.dto;

import java.util.List;

import com.studygroup.studygroup.Models.*;
import lombok.Data;

@Data
public class GroupDetailsDTO {
    private Integer id;
    private String subject;
    private String description;
    private String meetingType;
    private String location;
    private java.time.LocalDateTime meetingTime;
    private Integer maxMembers;

    private String creatorName;
    private List<String> members;
    private List<String> materials;
}
