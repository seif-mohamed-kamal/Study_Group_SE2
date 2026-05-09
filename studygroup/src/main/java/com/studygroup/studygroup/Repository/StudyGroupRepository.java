package com.studygroup.studygroup.Repository;

import com.studygroup.studygroup.Enums.GroupStatus;
import com.studygroup.studygroup.Models.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public interface StudyGroupRepository
        extends JpaRepository<StudyGroup, Integer> {
        List<StudyGroup> findByCreatorIdAndStatus(String creatorId, GroupStatus status);

        List<StudyGroup> findByStatus(GroupStatus status);
}