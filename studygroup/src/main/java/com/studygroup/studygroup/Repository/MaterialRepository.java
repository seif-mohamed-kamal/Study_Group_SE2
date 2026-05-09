package com.studygroup.studygroup.Repository;

import com.studygroup.studygroup.Models.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {

    @Query("""
        SELECT DISTINCT m FROM Material m
        JOIN FETCH m.file
        JOIN FETCH m.uploader gm
        JOIN FETCH gm.user
        WHERE m.group.id = :groupId
        ORDER BY m.uploadedAt DESC
    """)
    List<Material> findByGroupIdWithDetails(Integer groupId);
}