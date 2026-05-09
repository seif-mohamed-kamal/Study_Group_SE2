package com.adminmicroservice.adminmicroservice.Models;


import com.adminmicroservice.adminmicroservice.Enums.GroupStatus;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "study_group")
@Data
public class StudyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "creator_id")
    private String creatorId;

    @ManyToOne
    @JoinColumn(name = "creator_id", insertable = false, updatable = false)
    private GroupCreator creator;

    private String subject;
    private String description;
    private Integer maxMembers;
    private String meetingType;
    private java.time.LocalDateTime meetingTime;
    private String location;

    @Enumerated(EnumType.STRING)
    private GroupStatus status;
}
