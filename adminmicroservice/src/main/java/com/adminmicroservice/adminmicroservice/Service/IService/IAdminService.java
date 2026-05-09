package com.adminmicroservice.adminmicroservice.Service.IService;

import java.util.List;

import com.adminmicroservice.adminmicroservice.dto.GroupCreatorDTO;
import com.adminmicroservice.adminmicroservice.dto.ResponseDTO;
import com.adminmicroservice.adminmicroservice.dto.StudyGroupDTO;



public interface IAdminService {

    // Creator Accounts 
    ResponseDTO<List<GroupCreatorDTO>> getAllAccountRequests();

    ResponseDTO<String> acceptCreatorAccount(String userId);

    ResponseDTO<String> rejectCreatorAccount(String userId);

  
    // Groups
    ResponseDTO<List<StudyGroupDTO>> getWaitingGroups();

    ResponseDTO<String> acceptGroup(Integer groupId);

    ResponseDTO<String> rejectGroup(Integer groupId);
}
