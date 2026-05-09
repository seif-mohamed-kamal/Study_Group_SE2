package com.studygroup.studygroup.Repository;
import com.studygroup.studygroup.Models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; 
@Repository
public interface GroupCreatorRepository extends JpaRepository<GroupCreator,String> {

    Optional<GroupCreator> findByUserId(String userId);
} 