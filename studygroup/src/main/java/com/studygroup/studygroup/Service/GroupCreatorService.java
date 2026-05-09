package com.studygroup.studygroup.Service;

import java.util.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.studygroup.studygroup.Enums.CreatorStatus;
import com.studygroup.studygroup.Enums.GroupStatus;
import com.studygroup.studygroup.Enums.RequestStatus;
import com.studygroup.studygroup.Models.GroupCreator;
import com.studygroup.studygroup.Models.GroupMember;
import com.studygroup.studygroup.Models.GroupMemberId;
import com.studygroup.studygroup.Models.JoinRequest;
import com.studygroup.studygroup.Models.StudyGroup;
import com.studygroup.studygroup.Repository.GroupCreatorRepository;
import com.studygroup.studygroup.Repository.GroupMemberRepository;
import com.studygroup.studygroup.Repository.JoinRequestRepository;
import com.studygroup.studygroup.Repository.*;
import com.studygroup.studygroup.Service.IService.IGroupCreatorService;
import com.studygroup.studygroup.Service.IService.ICurrentUserService;
import com.studygroup.studygroup.dto.CreatorGroupsDTO;
import com.studygroup.studygroup.dto.CreatorRequestDTO;
import com.studygroup.studygroup.dto.ResponseDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class GroupCreatorService implements IGroupCreatorService {
    
  
    private final GroupCreatorRepository creatorRepo;
  
    private final JoinRequestRepository requestRepo;
  
    private final GroupMemberRepository groupMemberRepo;

    private final  StudyGroupRepository StudyGroupRepo ;

    private final ICurrentUserService currentUser;

  //@Override
 public ResponseDTO<List<CreatorRequestDTO>> getAllRequests() {

    String creatorId = currentUser.getUserId();
    //String creatorId = currentUser.getUserId();

    List<JoinRequest> requests =
        requestRepo.findByGroup_CreatorIdAndStatus(
                creatorId,
                RequestStatus.PENDING
        );

    if (requests == null || requests.isEmpty()) {
        return new ResponseDTO<>(
                false,
                "No requests found",
                new ArrayList<>()
        );
    }

    List<CreatorRequestDTO> data = requests.stream()
            .map(r -> {
                CreatorRequestDTO dto = new CreatorRequestDTO();
                dto.setRequestId(r.getId());
                dto.setStudentId(r.getRequestorId());
                dto.setStudentName(r.getRequestor().getName());
                dto.setGroupId(r.getGroupId());
                dto.setGroupSubject(r.getGroup().getSubject());
                dto.setStatus(r.getStatus());
                dto.setRequestedAt(r.getRequestedAt());
                return dto;
            })
            .toList();

    return new ResponseDTO<>(
            true,
            "Requests retrieved successfully",
            data
    );
}

public ResponseDTO<List<CreatorRequestDTO>> getRequestsByGroup(Integer groupId) {

    String creatorId = currentUser.getUserId();

    List<JoinRequest> requests =
            requestRepo.findByGroupIdAndGroup_CreatorIdAndStatus(
                    groupId,
                    creatorId,
                    RequestStatus.PENDING
            );

    if (requests == null || requests.isEmpty()) {
        return new ResponseDTO<>(
                false,
                "No requests found for this group",
                new ArrayList<>()
        );
    }

    List<CreatorRequestDTO> data = requests.stream()
            .map(r -> {
                CreatorRequestDTO dto = new CreatorRequestDTO();
                dto.setRequestId(r.getId());
                dto.setStudentId(r.getRequestorId());
                dto.setStudentName(
                        r.getRequestor() != null ? r.getRequestor().getName() : null
                );
                dto.setGroupId(r.getGroupId());
                dto.setGroupSubject(
                        r.getGroup() != null ? r.getGroup().getSubject() : null
                );
                dto.setStatus(r.getStatus());
                dto.setRequestedAt(r.getRequestedAt());
                return dto;
            })
            .toList();

    return new ResponseDTO<>(
            true,
            "Requests retrieved successfully",
            data
    );
}


@Transactional
public ResponseDTO<String> acceptRequest(Integer requestId) {

    JoinRequest request = requestRepo
            .findById(requestId)
            .orElse(null);

    if (request == null) {
        return new ResponseDTO<>(
                false,
                "Request not found",
                null
        );
    }

    if (request.getStatus() == RequestStatus.ACCEPTED) {
        return new ResponseDTO<>(
                false,
                "Request already accepted",
                null
        );
    }

    if (request.getStatus() == RequestStatus.REJECTED) {
    return new ResponseDTO<>(
            false,
            "Cannot accept a rejected request",
            null
    );
}

    request.setStatus(RequestStatus.ACCEPTED);

    GroupMember member = new GroupMember();

    GroupMemberId id = new GroupMemberId(
            request.getRequestorId(),
            request.getGroupId()
    );

    member.setId(id);
    member.setUser(request.getRequestor());
    member.setGroup(request.getGroup());
    member.setIsCreator(false);

    groupMemberRepo.save(member);

    requestRepo.save(request);

    return new ResponseDTO<>(
            true,
            "Request accepted successfully",
            null
    );
}


@Transactional
public ResponseDTO<String> rejectRequest(Integer requestId) {

    JoinRequest request = requestRepo
            .findById(requestId)
            .orElse(null);

    if (request == null) {
        return new ResponseDTO<>(
                false,
                "Request not found",
                null
        );
    }

    if (request.getStatus() == RequestStatus.REJECTED) {
        return new ResponseDTO<>(
                false,
                "Request already rejected",
                null
        );
    }

    if (request.getStatus() == RequestStatus.ACCEPTED) {
    return new ResponseDTO<>(
            false,
            "Cannot reject an accepted request",
            null
    );
}

    request.setStatus(RequestStatus.REJECTED);

    requestRepo.save(request);

    return new ResponseDTO<>(
            true,
            "Request rejected successfully",
            null
    );
}


public ResponseDTO<List<CreatorGroupsDTO>> getCreatorGroups() {

    String creatorId = currentUser.getUserId();

    List<StudyGroup> groups =
            StudyGroupRepo.findByCreatorIdAndStatus(
                    creatorId,
                    GroupStatus.APPROVED
            );

    if (groups == null || groups.isEmpty()) {
        return new ResponseDTO<>(
                false,
                "No groups found for this creator",
                new ArrayList<>()
        );
    }

    List<CreatorGroupsDTO> data = groups.stream()
            .map(g -> {
                CreatorGroupsDTO dto = new CreatorGroupsDTO();
                dto.setId(g.getId());
                dto.setSubject(g.getSubject());
                dto.setDescription(g.getDescription());
                dto.setMaxMembers(g.getMaxMembers());
                dto.setMeetingTime(g.getMeetingTime());
                dto.setMeetingType(g.getMeetingType());
                dto.setLocation(g.getLocation());
                return dto;
            })
            .toList();

    return new ResponseDTO<>(
            true,
            "Groups retrieved successfully",
            data
    );
}

        @Override
        public Map.Entry<Boolean, String> canCreatorLogin(String userId) {

                GroupCreator creator = creatorRepo.findByUserId(userId).orElse(null);

                if (creator == null)
                return new AbstractMap.SimpleEntry<>(true, "");

                if (creator.getStatus() != CreatorStatus.APPROVED)
                return new AbstractMap.SimpleEntry<>(false, "Creator account is not approved yet");

                return new AbstractMap.SimpleEntry<>(true, "");
        }
}

