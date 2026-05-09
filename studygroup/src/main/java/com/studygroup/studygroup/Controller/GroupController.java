package com.studygroup.studygroup.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;

import com.studygroup.studygroup.dto.*;
import com.studygroup.studygroup.Models.StudyGroup;
import com.studygroup.studygroup.Service.*;
import com.studygroup.studygroup.Service.IService.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    private final ICurrentUserService currentUser;

    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO<List<GroupDTO>>> getAll() {
        var result = groupService.getAllGroups();
        if(!result.isSuccess())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<GroupDetailsDTO>> getById(@PathVariable Integer id){
        var result = groupService.getGroupById(id);
        if(!result.isSuccess())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/subject/{subject}")
    public ResponseEntity<ResponseDTO<List<GroupDTO>>> getBySubject(@PathVariable String subject){
        var result = groupService.getGroupBySubject(subject);
        if(!result.isSuccess())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<ResponseDTO<List<GroupDTO>>> getByLocation(@PathVariable String location){
        var result = groupService.getGroupByLocation(location);
        if(!result.isSuccess())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-time")
    public ResponseEntity<ResponseDTO<List<GroupDTO>>> getByMeetingTime(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to){
                    var result = groupService.getGroupByMeetingTime(from, to);
                    if(!result.isSuccess())
                        return ResponseEntity.notFound().build();

                    return ResponseEntity.ok(result);
    }
    
    
    @PostMapping
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<ResponseDTO<Integer>> createGroup(@Valid @RequestBody CreateGroupDTO dto){
        String userId = currentUser.getUserId();
        var result = groupService.createGroup(userId, dto);
        if(!result.isSuccess())
            return ResponseEntity.badRequest().body(result);
        return ResponseEntity.ok(result);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<ResponseDTO<String>> updateGroup(@PathVariable int id,@RequestBody CreateGroupDTO dto){
        String userId = currentUser.getUserId();
        var result = groupService.updateGroup(id, userId, dto);
        if(!result.isSuccess())
            return ResponseEntity.badRequest().body(result);
        return ResponseEntity.ok(result);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<ResponseDTO<String>> deleteGroup(@PathVariable int id){
        String userId = currentUser.getUserId();
        var result = groupService.deleteGroup(id, userId);
        if(!result.isSuccess())
            return ResponseEntity.badRequest().body(result);
        return ResponseEntity.ok(result);
    }
}
