package com.studygroup.studygroup.Service;

import com.studygroup.studygroup.Controller.GroupController;

import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.format.annotation.DateTimeFormat;

import com.studygroup.studygroup.dto.*;
import com.studygroup.studygroup.Enums.GroupStatus;
import com.studygroup.studygroup.Models.StudyGroup;
import com.studygroup.studygroup.Repository.GroupRepository;
import com.studygroup.studygroup.Repository.JoinRequestRepository;
import com.studygroup.studygroup.Repository.MaterialRepository;
import com.studygroup.studygroup.Repository.CommentRepository;
import com.studygroup.studygroup.Repository.GroupMemberRepository;
import com.studygroup.studygroup.Service.IService.IGroupService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService implements IGroupService{
    private final GroupRepository groupRepo;
    private final JoinRequestRepository joinRequestRepo;
    private final MaterialRepository materialRepo;
    private final CommentRepository commentRepo;
    private final GroupMemberRepository groupMemberRepo;

    @Override
    public ResponseDTO<List<GroupDTO>> getAllGroups() {

        var groups = groupRepo.findByStatus(GroupStatus.APPROVED);

        if (groups.isEmpty()) {
            return new ResponseDTO<>(false, "No groups found", null);
        }

        var data = groups.stream().map(g -> {
            GroupDTO dto = new GroupDTO();
            dto.setId(g.getId());
            dto.setSubject(g.getSubject());
            dto.setDescription(g.getDescription());
            dto.setMeetingType(g.getMeetingType());
            dto.setLocation(g.getLocation());
            dto.setMeetingTime(g.getMeetingTime());
            dto.setMaxMembers(g.getMaxMembers());
            dto.setCreatorName(g.getCreator().getUser().getName());
            return dto;
        }).toList();

        return new ResponseDTO<>(true, "Success", data);
    }

    @Override
    public ResponseDTO<GroupDetailsDTO> getGroupById(Integer id){
        var group = groupRepo.findById(id).orElse(null);
        if(group == null)
            return new ResponseDTO<>(false,"Not Found",null);

        if(group.getStatus() != GroupStatus.APPROVED)
            return new ResponseDTO<>(false,"Not available",null);

        GroupDetailsDTO dto = new GroupDetailsDTO();
        dto.setId(group.getId());
        dto.setSubject(group.getSubject());
        dto.setDescription(group.getDescription());
        dto.setMeetingTime(group.getMeetingTime());
        dto.setLocation(group.getLocation());
        dto.setMeetingType(group.getMeetingType());
        dto.setMaxMembers(group.getMaxMembers());
        dto.setCreatorName(group.getCreator().getUser().getName());

        return new ResponseDTO<>(true,"Success",dto);
    }

    @Override
    public ResponseDTO<List<GroupDTO>> getGroupBySubject(String subject){
        if(subject == null || subject.isBlank())
            return new ResponseDTO<>(false,"Subject is required",null);
        var groups = groupRepo.findByStatusAndSubjectContainingIgnoreCase(GroupStatus.APPROVED, subject);
        if(groups.isEmpty())
            return new ResponseDTO<>(false,"No groups found",null);
        
        var result = groups.stream().map(g -> {
            GroupDTO dto = new GroupDTO();
            dto.setId(g.getId());
            dto.setSubject(g.getSubject());
            dto.setDescription(g.getDescription());
            dto.setMeetingType(g.getMeetingType());
            dto.setLocation(g.getLocation());
            dto.setMeetingTime(g.getMeetingTime());
            dto.setMaxMembers(g.getMaxMembers());
            dto.setCreatorName(g.getCreator().getUser().getName());
            return dto;
        }).toList();

        return new ResponseDTO<>(true,"Success",result);
    }

    @Override
    public ResponseDTO<List<GroupDTO>> getGroupByLocation(String location){
        if(location == null || location.isBlank())
            return new ResponseDTO<>(false,"Location is required",null);

        var groups = groupRepo.findByStatusAndLocationContainingIgnoreCase(GroupStatus.APPROVED, location);

        if(groups.isEmpty())
            return new ResponseDTO<>(false,"No groups found",null);
        
        var result = groups.stream().map(g -> {
            GroupDTO dto = new GroupDTO();
            dto.setId(g.getId());
            dto.setSubject(g.getSubject());
            dto.setDescription(g.getDescription());
            dto.setMeetingType(g.getMeetingType());
            dto.setLocation(g.getLocation());
            dto.setMeetingTime(g.getMeetingTime());
            dto.setMaxMembers(g.getMaxMembers());
            dto.setCreatorName(g.getCreator().getUser().getName());
            return dto;
        }).toList();

        return new ResponseDTO<>(true,"Success",result);
    }

    @Override
    public ResponseDTO<List<GroupDTO>> getGroupByMeetingTime(LocalDateTime from,LocalDateTime to){
        if(from == null && to == null)
            return new ResponseDTO<>(false,"At least one date required",null);

        List<StudyGroup> groups = null;
        
        if(from != null && to != null)
            groups=groupRepo.findByMeetingTimeBetweenAndStatus(from, to , GroupStatus.APPROVED);
        else if (from !=null)
            groups=groupRepo.findByMeetingTimeAfterAndStatus(from, GroupStatus.APPROVED);
        else if (to != null)
            groups=groupRepo.findByMeetingTimeBeforeAndStatus(to,GroupStatus.APPROVED );

        if(groups.isEmpty())
            return new ResponseDTO<>(false,"No groups found",null);

        var result = groups.stream().map(g -> {
            GroupDTO dto = new GroupDTO();
            dto.setId(g.getId());
            dto.setSubject(g.getSubject());
            dto.setDescription(g.getDescription());
            dto.setMeetingType(g.getMeetingType());
            dto.setLocation(g.getLocation());
            dto.setMeetingTime(g.getMeetingTime());
            dto.setMaxMembers(g.getMaxMembers());
            dto.setCreatorName(g.getCreator().getUser().getName());
            return dto;
        }).toList();

        return new ResponseDTO<>(true,"Success",result);
    }
    
    @Override
    public ResponseDTO<Integer> createGroup(String userId, CreateGroupDTO dto){
        StudyGroup group = new StudyGroup();
        group.setCreatorId(userId);
        group.setSubject(dto.getSubject());
        group.setDescription(dto.getDescription());
        group.setMaxMembers(dto.getMaxMembers());
        group.setMeetingType(dto.getMeetingType());
        group.setLocation(dto.getLocation());
        group.setMeetingTime(dto.getMeetingTime());
        group.setStatus(GroupStatus.PENDING);

        groupRepo.save(group);
        
        return new ResponseDTO<>(true, "Created", group.getId());
    }

    @Override
    public ResponseDTO<String> updateGroup(Integer id, String userId, CreateGroupDTO dto){
        var group = groupRepo.findById(id).orElse(null);
        if(group == null)
            return new ResponseDTO<>(false,"Not Found",null);

        // if(!group.getCreatorId().equals(userId))
        //     return new ResponseDTO<>(false,"Not Allowed",null);

        group.setSubject(dto.getSubject());
        group.setDescription(dto.getDescription());
        group.setMaxMembers(dto.getMaxMembers());
        group.setMeetingType(dto.getMeetingType());
        group.setLocation(dto.getLocation());
        group.setMeetingTime(dto.getMeetingTime());

        groupRepo.save(group);
        return new ResponseDTO<>(true,"Updated Successfully",null);
    }

    @Override 
    public ResponseDTO<String> deleteGroup(Integer id,String userId){

        var group = groupRepo.findById(id).orElse(null);

        if(group == null)
            return new ResponseDTO<>(false,"Not Found",null);

        if(!group.getCreatorId().equals(userId))
            return new ResponseDTO<>(false,"Not Allowed",null);

        try {
            var groupMembers = groupMemberRepo.findByGroup_Id(id);
            if (!groupMembers.isEmpty()) {
                groupMemberRepo.deleteAll(groupMembers);
            }

            var materials = materialRepo.findByGroupIdWithDetails(id);

            if (!materials.isEmpty()) {

                for (var material : materials) {

                    var comments = commentRepo
                            .findByMaterialIdOrderByCreatedAtDesc(material.getId());

                    if (!comments.isEmpty()) {
                        commentRepo.deleteAll(comments);
                    }
                }

                materialRepo.deleteAll(materials);
            }
            joinRequestRepo.deleteByGroup_Id(id);
            groupRepo.delete(group);

            return new ResponseDTO<>(true,"Deleted Successfully",null);

        } catch (Exception e) {

            return new ResponseDTO<>(
                false,
                "Error deleting group: " + e.getMessage(),
                null
            );
        }
    }  

}
