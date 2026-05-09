package com.studygroup.studygroup.Controller;

import com.studygroup.studygroup.dto.*;
import com.studygroup.studygroup.Service.IService.IMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material")
@RequiredArgsConstructor
public class MaterialController {

    private final IMaterialService materialService;

   @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public ResponseEntity<ResponseDTO<Boolean>> addMaterial(
            @RequestBody AddMaterialDTO dto
    ) {
        return ResponseEntity.ok(materialService.addMaterial(dto));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/{groupId}")
    public ResponseEntity<ResponseDTO<List<MaterialResponseDTO>>> getMaterials(
            @PathVariable Integer groupId
    ) {
        return ResponseEntity.ok(materialService.getMaterial(groupId));
    }
}