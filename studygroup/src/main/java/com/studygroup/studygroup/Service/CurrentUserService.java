package com.studygroup.studygroup.Service;

import com.studygroup.studygroup.Service.IService.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService implements ICurrentUserService {

    @Override
    public String getUserId() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) {
            return null;
        }

        return auth.getName(); 
    }
}