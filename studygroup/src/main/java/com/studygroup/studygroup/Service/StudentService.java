package com.studygroup.studygroup.Service;

import com.studygroup.studygroup.Enums.RequestStatus;
import com.studygroup.studygroup.Models.JoinRequest;
import com.studygroup.studygroup.Models.StudyGroup;
import com.studygroup.studygroup.Repository.StudentRepository;
import com.studygroup.studygroup.Service.IService.IStudentService;
import com.studygroup.studygroup.Service.IService.ICurrentUserService;
import com.studygroup.studygroup.dto.ResponseDTO;
import com.studygroup.studygroup.dto.StudentGroupDTO;
import com.studygroup.studygroup.dto.StudentRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService {

    private final StudentRepository repo;
    private final ICurrentUserService currentUser;

    @Override
    public ResponseDTO<String> requestToJoinGroup(int groupId) {
        String userId = currentUser.getUserId();
        StudyGroup group = repo.getGroupByGroupId(groupId);

        if (group == null) {
            return new ResponseDTO<>(false, "Group not found", null);
        }

        List<JoinRequest> exist = repo.getAllCassesRequests(userId);
        Optional<JoinRequest> existing = exist.stream()
                .filter(r -> r.getGroupId() == groupId)
                .findFirst();

        if (existing.isPresent()) {
            String message = switch (existing.get().getStatus()) {
                case PENDING -> "Your request is still pending";
                case ACCEPTED -> "You are already a member";
                case REJECTED -> "Your request was rejected";
                default -> "You already requested before";
            };

            return new ResponseDTO<>(false, message, null);
        }

        JoinRequest request = new JoinRequest();
        request.setGroupId(groupId);
        request.setRequestorId(userId);
        request.setStatus(RequestStatus.PENDING);
        request.setRequestedAt(LocalDateTime.now());

        repo.save(request);

        return new ResponseDTO<>(true, "Request sent successfully", null);
    }

    @Override
    public ResponseDTO<List<StudentRequestDTO>> getMyRequests() {
        String userId = currentUser.getUserId();
        List<JoinRequest> requests = repo.getStudentRequests(userId);

        if (requests == null || requests.isEmpty()) {
            return new ResponseDTO<>(false, "No requests found", List.of());
        }

        List<StudentRequestDTO> data = requests.stream().map(r -> {
            StudentRequestDTO dto = new StudentRequestDTO();
            dto.setRequestId(r.getId());
            dto.setGroupId(r.getGroupId());
            dto.setGroupSubject(r.getGroup() != null ? r.getGroup().getSubject() : "");
            dto.setGroupDescription(r.getGroup() != null ? r.getGroup().getDescription() : "");
            dto.setStatus(r.getStatus());
            dto.setRequestedAt(r.getRequestedAt());
            dto.setCreatorName(r.getGroup() != null && r.getGroup().getCreator() != null && 
                              r.getGroup().getCreator().getUser() != null ? 
                              r.getGroup().getCreator().getUser().getName() : "N/A");
            return dto;
        }).collect(Collectors.toList());

        return new ResponseDTO<>(true, "Requests retrieved successfully", data);
    }

    @Override
    public ResponseDTO<List<StudentGroupDTO>> getMyGroups() {
        String userId = currentUser.getUserId();
        List<JoinRequest> groups = repo.getStudentGroups(userId);

        if (groups == null || groups.isEmpty()) {
            return new ResponseDTO<>(false, "No joined groups found", List.of());
        }

        List<StudentGroupDTO> data = groups.stream().map(group -> {
            StudentGroupDTO dto = new StudentGroupDTO();
            dto.setId(group.getGroup().getId());
            dto.setSubject(group.getGroup().getSubject());
            dto.setDescription(group.getGroup().getDescription());
            dto.setCreatorName(group.getGroup().getCreator() != null && 
                              group.getGroup().getCreator().getUser() != null ? 
                              group.getGroup().getCreator().getUser().getName() : "N/A");
            dto.setMeetingType(group.getGroup().getMeetingType());
            dto.setLocation(group.getGroup().getLocation());
            return dto;
        }).collect(Collectors.toList());

        return new ResponseDTO<>(true, "Groups retrieved successfully", data);
    }
}
