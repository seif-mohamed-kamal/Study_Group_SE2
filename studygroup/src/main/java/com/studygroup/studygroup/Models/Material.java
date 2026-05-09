package com.studygroup.studygroup.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "material")
@Data
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    private LocalDateTime uploadedAt;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private StudyGroup group;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "uploader_user_id", referencedColumnName = "user_id"),
        @JoinColumn(name = "uploader_group_id", referencedColumnName = "group_id")
    })
    private GroupMember uploader;

    @OneToOne(mappedBy = "material", cascade = CascadeType.ALL)
    private FileMaterial file;
}