package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.model.*;
import com.example.ourknowledgebackend.model.entities.*;
import com.example.ourknowledgebackend.service.Block;
import com.example.ourknowledgebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Common common;

    private final KnowledgeServiceImpl knowledgeService;

    private final UserDao userDao;

    private final ParticipationDao participationDao;

    private final FilterDao filterDao;

    @Value("${app.constants.default_filter_name}")
    private String filterName;

    @Value("${app.constants.admin_role}")
    private String adminRole;

    @Override
    public User login(String userName, String email, String role){
        if(!userDao.existsByEmail(email)){
            User newUser = new User(userName, email, role, null);
            userDao.save(newUser);
            if(role.equals(adminRole)){
                Filter filter = new Filter(newUser, filterName);
                filterDao.save(filter);
            }
        }
        return userDao.findByEmail(email);

    }

    @Override
    public Block<UserResult> listUsers(int page, int size, String keywords, Long filterId) throws InstanceNotFoundException {
        List<Long> mandatoryList = new ArrayList<>();
        List<Long> recommendedList = new ArrayList<>();
        if(filterId!=null){
            Filter filter = filterDao.findById(filterId).orElseThrow(() -> new InstanceNotFoundException("project.entity.filter", filterId));;
            Map<String, List<Long>> filterParamTechnologiesId = common.getFilterParamTechnologiesId(filter);
            mandatoryList = filterParamTechnologiesId.get("mandatory");
            recommendedList = filterParamTechnologiesId.get("recommended");
        }

        Slice<UserResult> slice = userDao.find(page, size,  keywords, mandatoryList, recommendedList);
        return new Block<>(slice.getContent(), slice.hasNext(), page, size);
    }

    @Override
    public UserProfile showProfile(Long userId) throws InstanceNotFoundException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.user", userId));

        List<Project> projects = participationDao.findAllByUser(user).stream()
                .map(Participation::getProject).distinct().collect(Collectors.toList());
        List<KnowledgeTree> knowledgeTreeList = knowledgeService.listUserKnowledge(user);

        return new UserProfile(user, projects, knowledgeTreeList);
    }


    @Override
    public void updateUser(Long userId, String startDate) throws InstanceNotFoundException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.user", userId));

        userDao.save(new User(user.getId(), user.getName(), user.getEmail(), user.getRole(), startDate));
    }

}
