package com.studygroup.studygroup.Service;

import com.studygroup.studygroup.dto.*;
import com.studygroup.studygroup.Models.*;
import com.studygroup.studygroup.Repository.*;
import com.studygroup.studygroup.Service.IService.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService implements IMaterialService {

    private final MaterialRepository materialRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final ICurrentUserService currentUser;

    @Override
    public ResponseDTO<Boolean> addMaterial(AddMaterialDTO dto) {

        String userId = currentUser.getUserId();

        GroupMember member = groupMemberRepository
                .findByUser_IdAndGroup_Id(userId, dto.getGroupId())
                .orElse(null);

        if (member == null) {
            return new ResponseDTO<>(false, "You are not a member of this group", false);
        }

        Material material = new Material();
        material.setGroup(member.getGroup());
        material.setUploader(member); 
        material.setDescription(dto.getDescription());
        material.setUploadedAt(LocalDateTime.now());

        FileMaterial file = new FileMaterial();
        file.setFileName(dto.getFileName());
        file.setFilePath(dto.getFilePath());
        file.setMaterial(material);

        material.setFile(file);

        materialRepository.save(material);

        return new ResponseDTO<>(true, "Material added successfully", true);
    }

    @Override
    public ResponseDTO<List<MaterialResponseDTO>> getMaterial(Integer groupId) {

        if (!studyGroupRepository.existsById(groupId)) {
            return new ResponseDTO<>(false, "Group not found", null);
        }

        List<Material> materials =
                materialRepository.findByGroupIdWithDetails(groupId);

        List<MaterialResponseDTO> result = materials.stream().map(m -> {

            MaterialResponseDTO dto = new MaterialResponseDTO();
            dto.setId(m.getId());
            dto.setDescription(m.getDescription());
            dto.setFileName(m.getFile().getFileName());
            dto.setFilePath(m.getFile().getFilePath());
            dto.setUploaderName(m.getUploader().getUser().getName());
            dto.setUploadedAt(m.getUploadedAt());

            return dto;

        }).toList();

        return new ResponseDTO<>(true, "Materials retrieved successfully", result);
    }
}