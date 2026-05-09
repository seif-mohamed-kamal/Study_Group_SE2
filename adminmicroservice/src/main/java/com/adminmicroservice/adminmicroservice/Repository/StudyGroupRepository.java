package com.adminmicroservice.adminmicroservice.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.adminmicroservice.adminmicroservice.Enums.GroupStatus;
import com.adminmicroservice.adminmicroservice.Models.StudyGroup;

import java.util.List;
@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Integer> {

    // 4. Get groups by creator
   List<StudyGroup> findByCreatorIdAndStatus(String creatorId, GroupStatus status);

   List<StudyGroup> findByStatus(GroupStatus status);
}