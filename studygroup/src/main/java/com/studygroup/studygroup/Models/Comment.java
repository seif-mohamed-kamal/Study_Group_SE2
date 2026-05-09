package com.studygroup.studygroup.Models;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "comment")
@Data

public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String text;

    private LocalDateTime createdAt;
}