package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.model.*;
import com.example.ourknowledgebackend.model.entities.User;
import com.example.ourknowledgebackend.model.entities.UserDao;
import com.example.ourknowledgebackend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final KnowledgeServiceImpl knowledgeService;

    private final UserDao userDao;

    @Override
    public User login(String userName, String password){
        return userDao.findByNameAndPassword(userName, password);
    }

    @Override
    public UserProfile showProfile(Long profileId, Long userId) {
        if (!userDao.existsById(profileId) || !userDao.existsById(userId)) {
            throw new EntityNotFoundException();
        }
        Optional<User> user = userDao.findById(profileId);
        List<KnowledgeTree> knowledgeTreeList = knowledgeService.listUserKnowledge(user.get());

        return new UserProfile(user.get(), knowledgeTreeList);
    }
}
