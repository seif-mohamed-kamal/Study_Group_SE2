package com.adminmicroservice.adminmicroservice.Models;


import com.adminmicroservice.adminmicroservice.Enums.CreatorStatus;

import jakarta.persistence.*;

import lombok.Data;

@Entity
@Table(name = "group_creator")
@Data
public class GroupCreator {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Enumerated(EnumType.STRING)
    private CreatorStatus status;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}