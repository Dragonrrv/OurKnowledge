package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.model.*;
import com.example.ourknowledgebackend.model.entities.*;
import com.example.ourknowledgebackend.service.Block;
import com.example.ourknowledgebackend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final KnowledgeServiceImpl knowledgeService;

    private final UserDao userDao;

    private final ParticipationDao participationDao;

    @Override
    public User login(String userName, String email, String role){
        if(!userDao.existsByEmail(email)){
            User newUser = new User(userName, email, role, null);
            userDao.save(newUser);
        }
        return userDao.findByEmail(email);

    }

    @Override
    public Block<User> listUsers(int page, String keywords, int size) {
        Slice<User> slice = userDao.find(page, keywords, size);
        return new Block<>(slice.getContent(), slice.hasNext(), page, size);
    }

    @Override
    public UserProfile showProfile(Long profileId, Long userId) {
        Optional<User> user = userDao.findById(profileId);
        if (!user.isPresent()) {
            throw new EntityNotFoundException();
        }
        List<Project> projects = participationDao.findAllByUser(user.get()).stream()
                .map(Participation::getProject).collect(Collectors.toList());
                List<KnowledgeTree> knowledgeTreeList = knowledgeService.listUserKnowledge(user.get());

        return new UserProfile(user.get(), projects, knowledgeTreeList);
    }
}
