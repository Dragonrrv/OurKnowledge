package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.exceptions.DuplicateInstanceException;
import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.exceptions.PermissionException;
import com.example.ourknowledgebackend.model.KnowledgeTree;
import com.example.ourknowledgebackend.model.KnownTechnology;
import com.example.ourknowledgebackend.model.SimpleVerification;
import com.example.ourknowledgebackend.model.entities.*;
import com.example.ourknowledgebackend.service.KnowledgeService;
import com.example.ourknowledgebackend.service.PermissionChecker;
import com.example.ourknowledgebackend.service.TechnologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.directory.InvalidAttributesException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KnowledgeServiceImpl implements KnowledgeService {

    private final PermissionChecker permissionChecker;

    private final Common common;

    private final TechnologyService technologyService;

    private final KnowledgeDao knowledgeDao;

    private final UserDao userDao;

    private final TechnologyDao technologyDao;

    private final ExtendedTechnologyDao extendedTechnologyDao;

    private final VerificationDao verificationDao;

    public List<KnowledgeTree> listUserKnowledge(User user) {
        List<KnownTechnology> knownTechnologyList = extendedTechnologyDao.findTechnologiesWithKnowledge(user.getId());

        for (KnownTechnology knownTechnology : knownTechnologyList) {
            List<Verification> verificationList = verificationDao.findAllByKnowledgeId(knownTechnology.getKnowledgeId());
            knownTechnology.setVerificationList(verificationList.stream()
                    .map(SimpleVerification::new)
                    .collect(Collectors.toList()));
        }

        return common.ListToTreeList(knownTechnologyList, KnowledgeTree::new);
    }

    @Override
    public List<Knowledge> addKnowledge(Long userId, Long technologyId, String technologyName, Long parentTechnologyId) throws InstanceNotFoundException, DuplicateInstanceException, InvalidAttributesException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.user", userId));
        Technology technology;
        if(technologyId==null){
            if(technologyName!=null){
                try {
                    technologyService.addTechnology(technologyName, parentTechnologyId, false);
                } catch (PermissionException | DuplicateInstanceException ignored) {}
                technology = technologyDao.findByNameAndParentId(technologyName, parentTechnologyId).get();
            } else {
                throw new InvalidAttributesException();
            }
        } else {
            technology = permissionChecker.checkTechnology(technologyId);
            Optional<Knowledge> knowledge = knowledgeDao.findByUserAndTechnology(user, technology);
            if (knowledge.isPresent()) {
                throw new DuplicateInstanceException("project.entities.knowledge", knowledge.get().getId());
            }
        }
        return addKnowledgeHierarchy(user, technology);
    }

    private List<Knowledge> addKnowledgeHierarchy(User user, Technology technology) {
        List<Knowledge> knowledges = new ArrayList<>();
        if (technology.getParentId() != null) {
            List<Knowledge> brotherKnowledges = knowledgeDao.findAllByUserAndTechnologyParentId(user, technology.getParentId());
            if (brotherKnowledges.isEmpty()) {
                Optional<Technology> parentTechnology = technologyDao.findById(technology.getParentId());
                parentTechnology.ifPresent(value -> knowledges.addAll(addKnowledgeHierarchy(user, value)));
            }
        }
        if(!knowledgeDao.existsByUserAndTechnology(user, technology)){
            knowledges.add(knowledgeDao.save(new Knowledge(user, technology, false, false)));
        }
        return knowledges;
    }

    @Override
    public void deleteKnowledge(Long userId, Long knowledgeId) throws InstanceNotFoundException, PermissionException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.user", userId));
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
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.user", userId));
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
