package com.adminmicroservice.adminmicroservice.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adminmicroservice.adminmicroservice.Models.GroupMember;
import com.adminmicroservice.adminmicroservice.Models.GroupMemberId;
@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {
}