package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.exceptions.DuplicateInstanceException;
import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.exceptions.PermissionException;
import com.example.ourknowledgebackend.model.*;
import com.example.ourknowledgebackend.model.entities.*;
import com.example.ourknowledgebackend.service.KnowledgeService;
import com.example.ourknowledgebackend.service.PermissionChecker;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KnowledgeServiceImpl implements KnowledgeService {

    private final PermissionChecker permissionChecker;

    private final KnowledgeDao knowledgeDao;

    private final TechnologyDao technologyDao;

    public List<KnowledgeTree> listUserKnowledge(User user) {
        List<Knowledge> knowledges = knowledgeDao.findAllByUser(user);

        Map<Long, List<Knowledge>> knowledgesMap = knowledges.stream()
                .collect(Collectors.groupingBy(know -> know.getTechnology().getParentId() != null ? know.getTechnology().getParentId() : 0L));

        KnowledgeTree root = fillKnowledgeTreeList(null, knowledgesMap, 0L);

        return root.getChildrenKnowledge();
    }

    private KnowledgeTree fillKnowledgeTreeList(Knowledge parent, Map<Long, List<Knowledge>> knowledgeMap, Long parentId) {
        List<Knowledge> childKnowledges = knowledgeMap.get(parentId);
        ArrayList<KnowledgeTree> childTreeList = new ArrayList<>();
        if (childKnowledges != null) {
            for (Knowledge knowledge : childKnowledges) {
                childTreeList.add(fillKnowledgeTreeList(knowledge, knowledgeMap, knowledge.getTechnology().getId()));
            }
        }
        return new KnowledgeTree(parent, childTreeList);
    }

    @Override
    public List<Knowledge> addKnowledge(Long userId, Long technologyId) throws InstanceNotFoundException, DuplicateInstanceException {
        User user = permissionChecker.checkUser(userId);
        Technology technology = permissionChecker.checkTechnology(technologyId);
        Optional<Knowledge> knowledge = knowledgeDao.findByUserAndTechnology(user, technology);
        if (knowledge.isPresent()) {
            throw new DuplicateInstanceException("project.entities.knowledge", knowledge.get().getId());
        }
        return addKnowledgeHieracy(user, technology);
    }

    public List<Knowledge> addKnowledgeHieracy(User user, Technology technology) {
        List<Knowledge> knowledges = new ArrayList<>();
        if (technology.getParentId() != null) {
            List<Knowledge> brotherKnowledges = knowledgeDao.findAllByUserAndTechnologyParentId(user, technology.getParentId());
            if (brotherKnowledges.isEmpty()) {
                Optional<Technology> parentTecnology = technologyDao.findById(technology.getParentId());
                parentTecnology.ifPresent(value -> knowledges.addAll(addKnowledgeHieracy(user, value)));
            }
        }
        knowledges.add(knowledgeDao.save(new Knowledge(user, technology, false, false)));
        return knowledges;
    }

    @Override
    public void deleteKnowledge(Long userId, Long knowledgeId) throws InstanceNotFoundException, PermissionException {
        User user = permissionChecker.checkUser(userId);
        Knowledge knowledge = permissionChecker.checkKnowledgeExistsAndBelongsTo(knowledgeId, userId);
        deleteKnowledgeHierarchy(user, knowledge);

    }

    private void deleteKnowledgeHierarchy(User user, Knowledge knowledge) {
        List<Knowledge> childrenKnowledges = knowledgeDao.findAllByUserAndTechnologyParentId(user, knowledge.getTechnology().getId());
        knowledgeDao.delete(knowledge);
        if (!childrenKnowledges.isEmpty()) {
            for(Knowledge childKnowledge : childrenKnowledges){
                deleteKnowledgeHierarchy(user, childKnowledge);
            }
        }
        if (!knowledge.getTechnology().isRelevant()) {
            List<Knowledge> knowledgeList = knowledgeDao.findAllByTechnology(knowledge.getTechnology());
            if (knowledgeList.isEmpty()) {
                technologyDao.delete(knowledge.getTechnology());
            }
        }
    }

    @Override
    public void updateKnowledge(Long userId, Long knowledgeId, Boolean mainSkill, Boolean likeIt) throws InstanceNotFoundException, PermissionException {
        User user = permissionChecker.checkUser(userId);
        Knowledge knowledge = permissionChecker.checkKnowledgeExistsAndBelongsTo(knowledgeId, userId);
        if (mainSkill != null) {
            knowledge.setMainSkill(mainSkill);
        }
        if (likeIt != null) {
            knowledge.setLikeIt(likeIt);
        }
        knowledgeDao.save(knowledge);
    }
}
