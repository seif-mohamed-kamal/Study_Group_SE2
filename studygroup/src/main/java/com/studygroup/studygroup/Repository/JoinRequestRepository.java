package com.studygroup.studygroup.Repository;

import com.studygroup.studygroup.Enums.RequestStatus;
import com.studygroup.studygroup.Models.*;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Transactional
public interface JoinRequestRepository extends JpaRepository<JoinRequest, Integer> {

    List<JoinRequest> findByGroup_CreatorIdAndStatus(String creatorId, RequestStatus status);
    void deleteByGroup_Id(Integer groupId);
    List<JoinRequest> findByGroupIdAndGroup_CreatorIdAndStatus(
        Integer groupId,
        String creatorId,
        RequestStatus status
);
}