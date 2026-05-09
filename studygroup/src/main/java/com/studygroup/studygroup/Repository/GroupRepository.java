package com.studygroup.studygroup.Repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.studygroup.studygroup.Models.*;

import java.time.LocalDateTime;
import java.util.List;
import com.studygroup.studygroup.Enums.GroupStatus;


@Repository
public interface GroupRepository extends JpaRepository<StudyGroup,Integer>{
    List<StudyGroup> findByStatus(GroupStatus status);
    List<StudyGroup> findByStatusAndSubjectContainingIgnoreCase(GroupStatus status, String subject);

    List<StudyGroup> findByStatusAndLocationContainingIgnoreCase(GroupStatus status, String location);

    // @Query("""
    //     SELECT g FROM StudyGroup g
    //     WHERE g.status = :status
    //     AND (:from IS NULL OR g.meetingTime >= :from)
    //     AND (:to IS NULL OR g.meetingTime <= :to)
    // """)
    List<StudyGroup> findByMeetingTimeBetweenAndStatus(LocalDateTime from, LocalDateTime to, GroupStatus status);

    List<StudyGroup> findByMeetingTimeAfterAndStatus(LocalDateTime from, GroupStatus status);

    List<StudyGroup> findByMeetingTimeBeforeAndStatus(LocalDateTime to, GroupStatus status);
}

