package com.studygroup.studygroup.Repository;

import com.studygroup.studygroup.Models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT c FROM Comment c WHERE c.material.id = :materialId ORDER BY c.createdAt DESC")
    List<Comment> findByMaterialIdOrderByCreatedAtDesc(@Param("materialId") Integer materialId);

    @Query("SELECT COUNT(gm) > 0 FROM GroupMember gm WHERE gm.user.id = :userId AND gm.group.id IN (SELECT m.group.id FROM Material m WHERE m.id = :materialId)")
    boolean isUserInMaterialGroup(@Param("userId") String userId, @Param("materialId") Integer materialId);
}
