package com.studygroup.studygroup.Repository;

import com.studygroup.studygroup.Models.GroupMember;
import com.studygroup.studygroup.Models.GroupMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository
        extends JpaRepository<GroupMember, GroupMemberId> {

    //for material 
    Optional<GroupMember> findByUser_IdAndGroup_Id(String userId, Integer groupId);

    List<GroupMember> findByGroup_Id(Integer groupId);
}