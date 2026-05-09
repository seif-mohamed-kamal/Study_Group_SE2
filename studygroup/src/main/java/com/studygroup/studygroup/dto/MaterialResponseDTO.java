package com.studygroup.studygroup.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MaterialResponseDTO {

    private Integer id;
    private String description;
    private String fileName;
    private String filePath;
    private String uploaderName;
    private LocalDateTime uploadedAt;
}