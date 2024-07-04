package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.exceptions.*;
import com.example.ourknowledgebackend.model.*;
import com.example.ourknowledgebackend.model.entities.*;
import com.example.ourknowledgebackend.service.KnowledgeService;
import com.example.ourknowledgebackend.service.PermissionChecker;
import com.example.ourknowledgebackend.service.TechnologyService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TechnologyServiceImpl implements TechnologyService {

    private final PermissionChecker permissionChecker;

    private final TechnologyDao technologyDao;

    private final KnowledgeDao knowledgeDao;

    @Override
    public List<TechnologyTree> listRelevantTechnologies() {
        List<Technology> allTechnologies = technologyDao.findAllByRelevantTrue();

        Map<Long, List<Technology>> technologiesMap = allTechnologies.stream()
                .collect(Collectors.groupingBy(tech -> tech.getParentId() != null ? tech.getParentId() : 0L));

        TechnologyTree root = fillTechnologyTreeList(null, technologiesMap, 0L);

        return root.getChildren();
    }

    private TechnologyTree fillTechnologyTreeList(Technology parent, Map<Long, List<Technology>> technologiesMap, Long parentId) {
        List<Technology> childTechnologies = technologiesMap.get(parentId);
        List<TechnologyTree> childTreeList = new ArrayList<>();
        if (childTechnologies != null) {
            for (Technology technology : childTechnologies) {
                childTreeList.add(fillTechnologyTreeList(technology, technologiesMap, technology.getId()));
            }
        }
        return new TechnologyTree(parent, childTreeList);
    }




    @Override
    public List<TechnologyTree> addTechnology(Long userId, String name, Long parentId, boolean relevant) throws InstanceNotFoundException, DuplicateInstanceException, PermissionException {
        User user = permissionChecker.checkUser(userId);
        if (parentId != null) {
            permissionChecker.checkTechnology(parentId);
        }
        if (relevant && !user.getRole().equals("Admin")) {
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
    public List<TechnologyTree> deleteTechnology(Long userId, Long technologyId, boolean deleteChildren) throws HaveChildrenException, PermissionException, InstanceNotFoundException {
        User user = permissionChecker.checkUser(userId);
        Technology technology = permissionChecker.checkTechnology(technologyId);
        if (!user.getRole().equals("Admin")) {
            throw new PermissionException();
        }
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
