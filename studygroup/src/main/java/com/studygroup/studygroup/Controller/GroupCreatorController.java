package com.studygroup.studygroup.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studygroup.studygroup.Service.GroupCreatorService;
import com.studygroup.studygroup.Service.IService.IGroupCreatorService;
import com.studygroup.studygroup.dto.CreatorGroupsDTO;
import com.studygroup.studygroup.dto.CreatorRequestDTO;
import com.studygroup.studygroup.dto.ResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/group-creator")
@RequiredArgsConstructor
public class GroupCreatorController {

    
    private final IGroupCreatorService service;

    @GetMapping("/joinRequests")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<ResponseDTO<List<CreatorRequestDTO>>> getAllRequests() {

        var result = service.getAllRequests();

        if (!result.isSuccess())
            return ResponseEntity.status(404).body(result);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/groupJoinRequests/{groupId}")
    @PreAuthorize("hasRole('CREATOR')")
    public  ResponseEntity<ResponseDTO<List<CreatorRequestDTO>>> getGroupRequests(@PathVariable Integer groupId) {

        var result = service.getRequestsByGroup(groupId);

        if (!result.isSuccess())
            return ResponseEntity.status(404).body(result);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/accept/request/{id}")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<ResponseDTO<String>> acceptRequest(@PathVariable Integer id) {

        var result = service.acceptRequest(id);

        if (!result.isSuccess())
            return ResponseEntity.badRequest().body(result);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/reject/request/{id}")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<ResponseDTO<String>> rejectRequest(@PathVariable Integer id) {

        var result = service.rejectRequest(id);

        if (!result.isSuccess())
            return ResponseEntity.badRequest().body(result);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/groups")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<ResponseDTO<List<CreatorGroupsDTO>>> getMyGroups() {

        var result = service.getCreatorGroups();

        if (!result.isSuccess())
            return ResponseEntity.status(404).body(result);

        return ResponseEntity.ok(result);
    }
}