package com.studygroup.studygroup.Service.IService;

import com.studygroup.studygroup.dto.AddCommentDTO;
import com.studygroup.studygroup.dto.CommentResponseDTO;
import com.studygroup.studygroup.dto.ResponseDTO;
import java.util.List;

public interface ICommentService {
    ResponseDTO<Boolean> addComment(AddCommentDTO dto);
    ResponseDTO<List<CommentResponseDTO>> getAllCommentsByMaterialId(int materialId);
}
