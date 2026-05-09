package com.studygroup.studygroup.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "file_material")
@Data
public class FileMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fileName;
    private String filePath;

    @OneToOne
    @JoinColumn(name = "material_id")
    private Material material;
}