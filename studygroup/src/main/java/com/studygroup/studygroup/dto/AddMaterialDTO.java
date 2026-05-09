package com.studygroup.studygroup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddMaterialDTO {

    @NotNull(message = "GroupId is required")
    private Integer groupId;

    @Size(max = 500, message = "Description max length is 500")
    private String description;

    @NotBlank(message = "FileName is required")
    @Size(max = 200)
    private String fileName;

    @NotBlank(message = "FilePath is required")
    @Size(max = 500)
    private String filePath;
}
