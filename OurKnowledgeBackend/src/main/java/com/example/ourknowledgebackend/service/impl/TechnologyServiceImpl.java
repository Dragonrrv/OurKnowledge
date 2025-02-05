package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.exceptions.*;
import com.example.ourknowledgebackend.model.*;
import com.example.ourknowledgebackend.model.entities.*;
import com.example.ourknowledgebackend.service.PermissionChecker;
import com.example.ourknowledgebackend.service.TechnologyService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TechnologyServiceImpl implements TechnologyService {

    private final PermissionChecker permissionChecker;
    
    @Value("${app.constants.admin_role}")
    private String adminRole;

    private final Common common;

    private final TechnologyDao technologyDao;

    private final KnowledgeDao knowledgeDao;

    private final UserDao userDao;

    @Override
    public List<TechnologyTree> listRelevantTechnologies() {
        List<Technology> relevantTechnologies = technologyDao.findAllByRelevantTrue();

        return common.ListToTreeList(relevantTechnologies, TechnologyTree::new);
    }

    @Override
    public List<TechnologyTree> addTechnology(Long userId, String name, Long parentId, boolean relevant) throws InstanceNotFoundException, DuplicateInstanceException, PermissionException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.user", userId));
        if (parentId != null) {
            permissionChecker.checkTechnology(parentId);
        }
        if (relevant && !user.getRole().equals(adminRole)) {
            throw new PermissionException();
        }
        Technology technology = permissionChecker.checkTechnology(name, parentId);

        if (technology == null) {
            technologyDao.save(new Technology(name, parentId, relevant));
        } else if (technology.isRelevant() || !relevant) {
            throw new DuplicateInstanceException("project.entity.technology", technology.getId());
        } else {
            technology.setRelevant(true);
            technologyDao.save(technology);
        }
        return listRelevantTechnologies();
    }

    @Override
    public List<TechnologyTree> deleteTechnology(Long technologyId, boolean deleteChildren) throws HaveChildrenException, PermissionException, InstanceNotFoundException {
        Technology technology = permissionChecker.checkTechnology(technologyId);
        if (deleteChildren) {
            deleteTechnologiesTree(technology);
        } else {
            List<Technology> childrenTechnologies = technologyDao.findAllByParentId(technology.getId());
            if (!childrenTechnologies.isEmpty()) {
                throw new HaveChildrenException();
            }
            if (knowledgeDao.existsByTechnology(technology)) {
                technology.setRelevant(false);
                technologyDao.save(technology);
            } else {
                technologyDao.delete(technology);
            }
        }
        return listRelevantTechnologies();
    }

    private void deleteTechnologiesTree(Technology technology){
        List<Technology> childrenTechnologies = technologyDao.findAllByParentId(technology.getId());
        if (!childrenTechnologies.isEmpty()) {
            for (Technology childTechnology : childrenTechnologies) {
                deleteTechnologiesTree(childTechnology);
            }
        }
        if (knowledgeDao.existsByTechnology(technology)) {
            technology.setRelevant(false);
            technologyDao.save(technology);
        } else {
            technologyDao.delete(technology);
        }
    }

}
