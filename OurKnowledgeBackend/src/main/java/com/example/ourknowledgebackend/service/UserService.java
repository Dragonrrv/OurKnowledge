package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.model.UserProfile;
import com.example.ourknowledgebackend.model.entities.User;

public interface UserService {

    User login(String userName, String password);
    UserProfile showProfile(Long profileId, Long userId);
}
