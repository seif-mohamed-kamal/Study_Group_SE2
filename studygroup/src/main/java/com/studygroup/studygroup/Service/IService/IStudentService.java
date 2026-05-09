package com.studygroup.studygroup.Service.IService;

import com.studygroup.studygroup.dto.ResponseDTO;
import com.studygroup.studygroup.dto.StudentGroupDTO;
import com.studygroup.studygroup.dto.StudentRequestDTO;
import java.util.List;

public interface IStudentService {
    ResponseDTO<String> requestToJoinGroup(int groupId);
    ResponseDTO<List<StudentRequestDTO>> getMyRequests();
    ResponseDTO<List<StudentGroupDTO>> getMyGroups();
}
