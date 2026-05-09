package com.studygroup.studygroup.dto;

import java.time.LocalDateTime;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Future;


import lombok.Data;

@Data
public class CreateGroupDTO {
    @NotBlank(message = "Subject is required")
    private String subject;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull(message = "Max members is required")
    @Min(value =2, message = "Min 2 members is required")
    private Integer maxMembers;
    @NotBlank(message = "Meeting type is required")
    private String meetingType;
    @NotNull(message = "Meeting time is required")
    @Future(message = "Meeting time must be in the future")
    private LocalDateTime meetingTime;
    @NotBlank(message = "Location is required")
    private String location = "Online";
}
