package com.studygroup.studygroup.Controller;

import com.studygroup.studygroup.Service.CommentService;
import com.studygroup.studygroup.dto.AddCommentDTO;
import com.studygroup.studygroup.dto.CommentResponseDTO;
import com.studygroup.studygroup.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDTO<Boolean>> addComment(@Valid @RequestBody AddCommentDTO dto) {
        ResponseDTO<Boolean> result = service.addComment(dto);
        
        if (!result.isSuccess()) {
            return ResponseEntity.status(403).body(result);
        }
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{materialId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDTO<List<CommentResponseDTO>>> getCommentsByMaterial(@PathVariable int materialId) {
        ResponseDTO<List<CommentResponseDTO>> result = service.getAllCommentsByMaterialId(materialId);
        return ResponseEntity.ok(result);
    }
}
