package com.studygroup.studygroup.Service;

import com.studygroup.studygroup.Models.*;
import com.studygroup.studygroup.Enums.*;
import com.studygroup.studygroup.Repository.*;
import com.studygroup.studygroup.Service.IService.*;
import com.studygroup.studygroup.Security.JwtService;
import com.studygroup.studygroup.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final GroupCreatorRepository groupCreatorRepository;
    private final IGroupCreatorService creatorService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ResponseDTO<Object> register(RegisterDTO dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return new ResponseDTO<>(false, "Email already exists", null);
        }

        if (dto.getRole() == null) {
            return new ResponseDTO<>(false, "Invalid role", null);
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());

        userRepository.save(user);

        if (dto.getRole()  == Role.CREATOR) {

            GroupCreator creator = new GroupCreator();
            creator.setUser(user);
            creator.setStatus(CreatorStatus.PENDING);

            groupCreatorRepository.save(creator);
        }

        return new ResponseDTO<>(
                true,
                "User registered successfully",
                new Object() {
                    public String id = user.getId();
                    public String email = user.getEmail();
                    public String name = user.getName();
                    public String role = user.getRole().name();
                }
        );
    }

    public ResponseDTO<LoginResultDTO> login(LoginDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElse(null);

        if (user == null) {
            return new ResponseDTO<>(false, "Invalid credentials", null);
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return new ResponseDTO<>(false, "Invalid credentials", null);
        }

        List<String> roles = List.of(user.getRole().name());

        Map.Entry<Boolean, String> validation = validateCreatorLogin(user);

        if (!validation.getKey()) {
            return new ResponseDTO<>(false, validation.getValue(), null);
        }
        
        String token = jwtService.generateToken(user);

        return new ResponseDTO<>(
                true,
                "Login successful",
                new LoginResultDTO(
                        token,
                        user.getEmail(),
                        user.getName(),
                        List.of(user.getRole().name())
                )
        );
    }

    private Map.Entry<Boolean, String> validateCreatorLogin(User user) {
        return creatorService.canCreatorLogin(user.getId());
    }
}