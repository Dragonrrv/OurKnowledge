package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.model.UserProfile;
import com.example.ourknowledgebackend.model.entities.User;

public interface UserService {

    User login(String userName, String email, String role);
    UserProfile showProfile(Long profileId, Long userId);
}
