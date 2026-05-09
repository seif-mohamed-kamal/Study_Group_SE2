package com.studygroup.studygroup.Service;

import com.studygroup.studygroup.Models.Comment;
import com.studygroup.studygroup.Models.Material;
import com.studygroup.studygroup.Models.User;
import com.studygroup.studygroup.Repository.CommentRepository;
import com.studygroup.studygroup.Service.IService.ICommentService;
import com.studygroup.studygroup.Service.IService.ICurrentUserService;
import com.studygroup.studygroup.dto.AddCommentDTO;
import com.studygroup.studygroup.dto.CommentResponseDTO;
import com.studygroup.studygroup.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final CommentRepository repo;
    private final ICurrentUserService currentUser;

    @Override
    public ResponseDTO<Boolean> addComment(AddCommentDTO dto) {
        String userId = currentUser.getUserId();

        boolean isMember = repo.isUserInMaterialGroup(userId, dto.getMaterialId());
        if (!isMember) {
            return new ResponseDTO<>(false, "You are not allowed to comment on this material", false);
        }

        Comment comment = new Comment();
        comment.setMaterial(new Material());
        comment.getMaterial().setId(dto.getMaterialId());
        comment.setUser(new User());
        comment.getUser().setId(userId);
        comment.setText(dto.getText());
        comment.setCreatedAt(LocalDateTime.now());

        repo.save(comment);

        return new ResponseDTO<>(true, "Comment added successfully", true);
    }

    @Override
    public ResponseDTO<List<CommentResponseDTO>> getAllCommentsByMaterialId(int materialId) {
        List<Comment> comments = repo.findByMaterialIdOrderByCreatedAtDesc(materialId);

        List<CommentResponseDTO> result = comments.stream().map(c -> {
            CommentResponseDTO dto = new CommentResponseDTO();
            dto.setId(c.getId());
            dto.setText(c.getText());
            dto.setUserName(c.getUser().getName());
            dto.setCreatedAt(c.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());

        return new ResponseDTO<>(true, "Comments retrieved successfully", result);
    }
}
