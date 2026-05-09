package com.studygroup.studygroup.Service.IService;

import java.util.AbstractMap;
import java.util.*;

import com.studygroup.studygroup.dto.CreatorGroupsDTO;
import com.studygroup.studygroup.dto.CreatorRequestDTO;
import com.studygroup.studygroup.dto.ResponseDTO;
public interface IGroupCreatorService {

    Map.Entry<Boolean, String> canCreatorLogin(String userId);

    ResponseDTO<List<CreatorRequestDTO>> getAllRequests();

    ResponseDTO<List<CreatorRequestDTO>> getRequestsByGroup(Integer groupId);

    ResponseDTO<String> acceptRequest(Integer requestId);

    ResponseDTO<String> rejectRequest(Integer requestId);

    ResponseDTO<List<CreatorGroupsDTO>> getCreatorGroups();
}
