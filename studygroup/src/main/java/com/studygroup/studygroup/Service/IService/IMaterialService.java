package com.studygroup.studygroup.Service.IService;

import com.studygroup.studygroup.dto.*;
import java.util.List;

public interface IMaterialService {

    ResponseDTO<Boolean> addMaterial(AddMaterialDTO dto);

    ResponseDTO<List<MaterialResponseDTO>> getMaterial(Integer groupId);
}