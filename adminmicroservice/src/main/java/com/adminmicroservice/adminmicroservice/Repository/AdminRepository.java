package com.adminmicroservice.adminmicroservice.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adminmicroservice.adminmicroservice.Enums.CreatorStatus;
import com.adminmicroservice.adminmicroservice.Models.GroupCreator;



public interface AdminRepository extends JpaRepository<GroupCreator, String> {


    List<GroupCreator> findByStatus(CreatorStatus status);
    Optional<GroupCreator> findById(String userId);
}