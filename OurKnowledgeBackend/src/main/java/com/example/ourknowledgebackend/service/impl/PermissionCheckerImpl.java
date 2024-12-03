package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.exceptions.PermissionException;
import com.example.ourknowledgebackend.model.entities.*;
import com.example.ourknowledgebackend.service.PermissionChecker;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PermissionCheckerImpl implements PermissionChecker {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TechnologyDao technologyDao;

    @Autowired
    private KnowledgeDao knowledgeDao;

    @Autowired
    private FilterDao filterDao;

    @Override
    public Long getUserIdByAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        return userDao.findByEmail(email).getId();
    }

    @Override
    public Technology checkTechnology(Long technologyId)
            throws InstanceNotFoundException {

        Optional<Technology> technology = technologyDao.findById(technologyId);

        if (!technology.isPresent()) {
            throw new InstanceNotFoundException("project.entities.technology", technologyId);
        }

        return technology.get();

    }

    @Override
    public Technology checkTechnology(String name, Long parentId)
            throws InstanceNotFoundException {

        Optional<Technology> technology = technologyDao.findByNameAndParentId(name, parentId);

        return technology.orElse(null);

    }

    @Override
    public Knowledge checkKnowledgeExistsAndBelongsTo(Long knowledgeId, Long userId) throws InstanceNotFoundException, PermissionException {
        Optional<Knowledge> knowledge = knowledgeDao.findById(knowledgeId);
        if (!knowledge.isPresent()) {
            throw new InstanceNotFoundException("project.entities.knowledge", knowledgeId);
        }
        if (!knowledge.get().getUser().getId().equals(userId)) {
            throw new PermissionException();
        }
        return knowledge.get();
    }

    @Override
    public Filter checkFilterExistsAndBelongsTo(Long filterId, Long userId) throws InstanceNotFoundException, PermissionException {
        Filter filter = filterDao.findById(filterId).orElseThrow(() -> new InstanceNotFoundException("project.entity.filter", filterId));
        if (!filter.getUser().getId().equals(userId)) {
            throw new PermissionException();
        }
        return filter;
    }

    @Override
    public Knowledge checkKnowledge(User user, Technology technology) throws InstanceNotFoundException {
        Optional<Knowledge> knowledge = knowledgeDao.findByUserAndTechnology(user, technology);

        return knowledge.orElse(null);
    }
}
