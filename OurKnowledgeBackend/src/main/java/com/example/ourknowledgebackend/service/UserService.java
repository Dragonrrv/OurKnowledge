package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.model.UserProfile;
import com.example.ourknowledgebackend.model.UserResult;
import com.example.ourknowledgebackend.model.entities.User;

import java.text.ParseException;

public interface UserService {

    User login(String userName, String email, String role);

    Block<UserResult> listUsers(int page, int size, String keywords, Long filterId) throws InstanceNotFoundException;

    UserProfile showProfile(Long profileId, Long userId) throws InstanceNotFoundException;

    void updateUser(Long userId, String startDate) throws InstanceNotFoundException, ParseException;
}
