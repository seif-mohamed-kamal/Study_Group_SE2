package com.adminmicroservice.adminmicroservice.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adminmicroservice.adminmicroservice.Service.IService.IAdminService;
import com.adminmicroservice.adminmicroservice.dto.GroupCreatorDTO;
import com.adminmicroservice.adminmicroservice.dto.ResponseDTO;
import com.adminmicroservice.adminmicroservice.dto.StudyGroupDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")

public class AdminController {
    private final IAdminService adminService;

    // Accounts
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/creator/requests")
    public ResponseEntity<ResponseDTO<List<GroupCreatorDTO>>> getAllRequests() {

       var result =
                adminService.getAllAccountRequests();

        if (!result.isSuccess()) {
            return ResponseEntity.status(404).body(result);
        }

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approve/account/{id}")
    public ResponseEntity<ResponseDTO<String>> approveCreatorAccount(@PathVariable String id) {
      var result = adminService.acceptCreatorAccount(id);

      if (!result.isSuccess()){
        return ResponseEntity.badRequest().body(result);
      }
      return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reject/account/{id}")
    public ResponseEntity<ResponseDTO<String>> rejectCreatorAccount(@PathVariable String id) {
      var result = adminService.rejectCreatorAccount(id);

      if (!result.isSuccess()){
        return ResponseEntity.badRequest().body(result);
      }
      return ResponseEntity.ok(result);
    }
    
   //Groups 
   @PreAuthorize("hasRole('ADMIN')")
   @GetMapping("/group/requests")
   public ResponseEntity<ResponseDTO<List<StudyGroupDTO>>>getWaitingGroups(){
    var result = adminService.getWaitingGroups();
    if(!result.isSuccess()){
        return ResponseEntity.status(404).body(result);

    }
     return ResponseEntity.ok(result);
   }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/accept/group/{id}")
    public ResponseEntity<ResponseDTO<String>>acceptGroup(@PathVariable Integer id){
       var result =
                adminService.acceptGroup(id);

        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reject/group/{id}")
    public ResponseEntity<ResponseDTO<String>> rejectGroup(@PathVariable Integer id) {

        var result =
                adminService.rejectGroup(id);

        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }
   
}
