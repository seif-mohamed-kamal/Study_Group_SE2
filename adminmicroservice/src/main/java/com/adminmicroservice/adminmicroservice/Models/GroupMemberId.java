package com.adminmicroservice.adminmicroservice.Models;
import java.io.Serializable;
import lombok.*;
import jakarta.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberId implements Serializable {

    private String userId;
    private Integer groupId;
}