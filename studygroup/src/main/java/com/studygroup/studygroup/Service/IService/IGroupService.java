package com.studygroup.studygroup.Service.IService;

import java.time.LocalDateTime;
import java.util.List;

import com.studygroup.studygroup.dto.*;


public interface IGroupService {

    ResponseDTO<List<GroupDTO>> getAllGroups();
    ResponseDTO<GroupDetailsDTO> getGroupById(Integer id);
    ResponseDTO<List<GroupDTO>> getGroupBySubject(String subject);
    ResponseDTO<List<GroupDTO>> getGroupByLocation(String location);
    ResponseDTO<List<GroupDTO>> getGroupByMeetingTime(LocalDateTime from,LocalDateTime to);
    ResponseDTO<Integer> createGroup(String userId, CreateGroupDTO dto);
    ResponseDTO<String> updateGroup(Integer id, String userId, CreateGroupDTO dto);
    ResponseDTO<String> deleteGroup(Integer id,String userId);
}
