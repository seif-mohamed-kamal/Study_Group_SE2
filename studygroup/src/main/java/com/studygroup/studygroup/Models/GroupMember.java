package com.studygroup.studygroup.Models;

import java.io.Serializable;
import lombok.*;
import jakarta.persistence.*;
@Entity
@Table(name = "group_member")
@Data
public class GroupMember {

    @EmbeddedId
    private GroupMemberId id;

    private Boolean isCreator;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private StudyGroup group;
}