package com.studygroup.studygroup.Models;
import com.studygroup.studygroup.Enums.RequestStatus;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "join_request")
@Data
public class JoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "group_id")
    private Integer groupId;

    @ManyToOne
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    private StudyGroup group;

    @Column(name = "requestor_id")
    private String requestorId;

    @ManyToOne
    @JoinColumn(name = "requestor_id", insertable = false, updatable = false)
    private User requestor;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private java.time.LocalDateTime requestedAt;
    private java.time.LocalDateTime respondedAt;
}