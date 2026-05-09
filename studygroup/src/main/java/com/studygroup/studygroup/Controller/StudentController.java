package com.studygroup.studygroup.Controller;

import com.studygroup.studygroup.Service.StudentService;
import com.studygroup.studygroup.dto.ResponseDTO;
import com.studygroup.studygroup.dto.StudentGroupDTO;
import com.studygroup.studygroup.dto.StudentRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    @PostMapping("/joinGroup/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ResponseDTO<String>> requestToJoin(@PathVariable int id) {
        ResponseDTO<String> result = service.requestToJoinGroup(id);
        
        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(result);
        }
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/myGroups")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ResponseDTO<List<StudentGroupDTO>>> getMyGroups() {
        ResponseDTO<List<StudentGroupDTO>> result = service.getMyGroups();
        
        if (!result.isSuccess()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/myRequests")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ResponseDTO<List<StudentRequestDTO>>> getMyRequests() {
        ResponseDTO<List<StudentRequestDTO>> result = service.getMyRequests();
        
        if (!result.isSuccess()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(result);
    }
}
