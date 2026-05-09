package com.adminmicroservice.adminmicroservice.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.adminmicroservice.adminmicroservice.Enums.CreatorStatus;
import com.adminmicroservice.adminmicroservice.Enums.GroupStatus;
import com.adminmicroservice.adminmicroservice.Models.GroupCreator;
import com.adminmicroservice.adminmicroservice.Models.GroupMember;
import com.adminmicroservice.adminmicroservice.Models.GroupMemberId;
import com.adminmicroservice.adminmicroservice.Models.StudyGroup;
import com.adminmicroservice.adminmicroservice.Repository.AdminRepository;
import com.adminmicroservice.adminmicroservice.Repository.GroupMemberRepository;
import com.adminmicroservice.adminmicroservice.Repository.StudyGroupRepository;
import com.adminmicroservice.adminmicroservice.Service.IService.IAdminService;
import com.adminmicroservice.adminmicroservice.dto.GroupCreatorDTO;
import com.adminmicroservice.adminmicroservice.dto.ResponseDTO;
import com.adminmicroservice.adminmicroservice.dto.StudyGroupDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService{
    private final AdminRepository adminRepository;
    private final StudyGroupRepository studyGroupRepo;
    private final GroupMemberRepository groupMemberRepo ;
    //private final ICurrentUserService currentUser;
   
   
    public ResponseDTO<List<GroupCreatorDTO>> getAllAccountRequests() {

        List<GroupCreator> creators = adminRepository.findByStatus(CreatorStatus.PENDING);

        if (creators == null || creators.isEmpty()) {
            return new ResponseDTO<>(
                    false,
                    "No pending creator accounts found",
                    new ArrayList<>()
            );
        }

        List<GroupCreatorDTO> data = creators.stream()
                .map(c -> {
                    GroupCreatorDTO dto = new GroupCreatorDTO();
                    dto.setUserId(c.getUserId());
                    dto.setEmail(
                            c.getUser() != null ? c.getUser().getEmail() : null
                    );
                    dto.setName(
                            c.getUser() != null ? c.getUser().getName() : null
                    );
                    dto.setStatus(c.getStatus());
                    return dto;
                })
                .toList();

        return new ResponseDTO<>(
                true,
                "Pending creator accounts retrieved successfully",
                data
        );
    }

    public ResponseDTO<String>acceptCreatorAccount(String userId)
    {
        GroupCreator creator = adminRepository.findById(userId)
        .orElse(null);

        if(creator == null)
        {
            return new ResponseDTO<>(
                false ,
                "Creator not found",
                null
            );
        }
        if(creator.getStatus() == CreatorStatus.APPROVED){
            return new ResponseDTO<>(
                false ,
                "Creator already approved",
                null
            );
        }
        if(creator.getStatus() == CreatorStatus.REJECTED){
            return new ResponseDTO<>(
                false ,
                "Creator was rejected , can't be approved ",
                null
            );
        }
        creator.setStatus(CreatorStatus.APPROVED);
        adminRepository.save(creator);
   
     return new ResponseDTO<>(
            true,
            "Creator approved successfully",
            null
    );
 }

    public ResponseDTO<String> rejectCreatorAccount(String userId) {

     GroupCreator creator = adminRepository.findById(userId)
        .orElse(null);

    if(creator == null)
        {
            return new ResponseDTO<>(
                false ,
                "Creator not found",
                null
            );
        }

    if (creator.getStatus() == CreatorStatus.REJECTED) {
        return new ResponseDTO<>(
                false,
                "Creator already rejected",
                null
        );
    }
    if (creator.getStatus() == CreatorStatus.APPROVED) {
        return new ResponseDTO<>(
                false,
                "Creator was approved , can't be rejected",
                null
        );
    }

    creator.setStatus(CreatorStatus.REJECTED);
    adminRepository.save(creator);

    return new ResponseDTO<>(
            true,
            "Creator rejected successfully",
            null
    );
}


    public ResponseDTO<List<StudyGroupDTO>> getWaitingGroups() {

    List<StudyGroup> groups = studyGroupRepo.findByStatus(GroupStatus.PENDING);

    if (groups == null || groups.isEmpty()) {
        return new ResponseDTO<>(
                false,
                "No pending groups found",
                new ArrayList<>()
        );
    }

    List<StudyGroupDTO> data = groups.stream()
            .map(group -> {
                StudyGroupDTO dto = new StudyGroupDTO();

                dto.setId(group.getId());
                dto.setSubject(group.getSubject());
                dto.setDescription(group.getDescription());
                dto.setMeetingType(group.getMeetingType());
                dto.setLocation(group.getLocation());

                dto.setCreatorName(
                        group.getCreator() != null &&
                        group.getCreator().getUser() != null
                                ? group.getCreator().getUser().getName()
                                : "N/A"
                );

                dto.setStatus(group.getStatus());
                dto.setMaxMembers(group.getMaxMembers());

                return dto;
            })
            .toList();

    return new ResponseDTO<>(
            true,
            "Pending groups retrieved successfully",
            data
    );

}

    @Transactional
    public ResponseDTO<String>acceptGroup(Integer groupId)
    {
        StudyGroup group = studyGroupRepo.findById(groupId).orElse(null);
 
    if (group == null) {
        return new ResponseDTO<>(
                false,
                "Group not found",
                null
        );
    }
    /// optional
    if (group.getCreator() == null || group.getCreator().getUser() == null) {
        return new ResponseDTO<>(
                false,
                "Creator data is missing",
                null
        );
    }

    if (group.getStatus() == GroupStatus.APPROVED) {
    return new ResponseDTO<>(
            false,
            "Group already approved",
            null
    );
}
    if (group.getStatus() == GroupStatus.REJECTED) {
        return new ResponseDTO<>(
                false,
                "Group was rejected, can't be approved",
                null
        );
    }
    group.setStatus(GroupStatus.APPROVED);

    ////add first member
    GroupMember creatorMember = new GroupMember();

    GroupMemberId id = new GroupMemberId(
            group.getCreatorId(),
            group.getId()
    );
    creatorMember.setId(id);
    creatorMember.setUser(group.getCreator().getUser());
    creatorMember.setGroup(group);
    creatorMember.setIsCreator(true);

    groupMemberRepo.save(creatorMember);
    studyGroupRepo.save(group);

    return new ResponseDTO<>(
            true,
            "Group approved successfully",
            null
    );

   }

   @Transactional
   public  ResponseDTO<String>rejectGroup(Integer groupId){
     StudyGroup group = studyGroupRepo.findById(groupId).orElse(null);
     if(group == null)
     {
        return new ResponseDTO<>(
                false,
                "Group not found",
                null
        );
     }
     if (group.getStatus() == GroupStatus.REJECTED) {
        return new ResponseDTO<>(
                false,
                "Group already rejected",
                null
        );
    }
     if (group.getStatus() == GroupStatus.APPROVED) {
        return new ResponseDTO<>(
                false,
                "Group was approved can't be rejected!",
                null
        );
    }

     group.setStatus(GroupStatus.REJECTED);

     studyGroupRepo.save(group);

    return new ResponseDTO<>(
            true,
            "Group rejected successfully",
            null
    );

   }
}
