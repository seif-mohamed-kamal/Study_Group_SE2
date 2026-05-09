package com.studygroup.studygroup.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.studygroup.studygroup.Enums.CreatorStatus;
import com.studygroup.studygroup.Models.GroupCreator;

@Repository
public interface AdminRepository extends JpaRepository<GroupCreator, String> {
    List<GroupCreator> findByStatus(CreatorStatus status);

    Optional<GroupCreator> findById(String userId);
}