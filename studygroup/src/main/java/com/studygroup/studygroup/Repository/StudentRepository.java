package com.studygroup.studygroup.Repository;

import com.studygroup.studygroup.Models.JoinRequest;
import com.studygroup.studygroup.Models.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<JoinRequest, Integer> {

    @Query("SELECT j FROM JoinRequest j WHERE j.requestorId = :userId")
    List<JoinRequest> getAllCassesRequests(@Param("userId") String userId);

    @Query("SELECT j FROM JoinRequest j WHERE j.requestorId = :userId AND j.status = 'ACCEPTED'")
    List<JoinRequest> getStudentGroups(@Param("userId") String userId);

    @Query("SELECT j FROM JoinRequest j WHERE j.requestorId = :userId AND j.status IN ('PENDING', 'ACCEPTED', 'REJECTED')")
    List<JoinRequest> getStudentRequests(@Param("userId") String userId);

    @Query("SELECT g FROM StudyGroup g WHERE g.id = :groupId")
    StudyGroup getGroupByGroupId(@Param("groupId") int groupId);
}
