package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.exceptions.DuplicateInstanceException;
import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.model.*;
import com.example.ourknowledgebackend.model.entities.*;
import com.example.ourknowledgebackend.service.Block;
import com.example.ourknowledgebackend.service.KnowledgeService;
import com.example.ourknowledgebackend.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.naming.directory.InvalidAttributesException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final Common common;

    private final KnowledgeService knowledgeService;

    private final ProjectDao projectDao;

    private final UsesDao usesDao;

    private final ParticipationDao participationDao;

    private final VerificationDao verificationDao;

    private final TechnologyDao technologyDao;

    private final ExtendedTechnologyDao extendedTechnologyDao;

    private final FilterDao filterDao;

    private final FilterParamDao filterParamDao;

    private final UserDao userDao;

    private final KnowledgeDao knowledgeDao;

    @Override
    public Block<Project> listProjects(int page, int size, String keywords, Long filterId) throws InstanceNotFoundException {
        List<Long> mandatoryList = new ArrayList<>();
        List<Long> recommendedList = new ArrayList<>();
        if(filterId!=null){
            Filter filter = filterDao.findById(filterId).orElseThrow(() -> new InstanceNotFoundException("project.entity.filter", filterId));;
            List<FilterParam> filterParamList = filterParamDao.findAllByFilter(filter);
            mandatoryList = filterParamList.stream()
                    .filter(FilterParam::isMandatory)
                    .map(filterParam -> filterParam.getTechnology().getId())
                    .collect(Collectors.toList());
            recommendedList = filterParamList.stream()
                    .filter(FilterParam::isRecommended)
                    .map(filterParam -> filterParam.getTechnology().getId())
                    .collect(Collectors.toList());
        }
        Slice<Project> slice = projectDao.find(page, size, keywords, mandatoryList, recommendedList);
        return new Block<>(slice.getContent(), slice.hasNext(), page, size);
    }

    @Override
    public ProjectDetails projectDetails(Long id) throws InstanceNotFoundException {
        Project project = projectDao.findById(id).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", id));
        List<UsesTree> usesTreeList = listProjectUses(project);
        List<Participation> participationList = participationDao.findAllByProjectOrderByStartDate(project);
        return new ProjectDetails(project, usesTreeList, participationList);
    }

    public List<UsesTree> listProjectUses(Project project) {
        List<UsesTechnology> usesTechnologyList = extendedTechnologyDao.findTechnologiesWithUses(project.getId());

        for (UsesTechnology uses : usesTechnologyList) {
            List<Verification> verificationList = verificationDao.findAllByUsesId(uses.getUsesId());
            uses.setVerificationList(verificationList.stream()
                    .map(SimpleVerification::new)
                    .collect(Collectors.toList()));
        }

        return common.ListToTreeList(usesTechnologyList, UsesTree::new);
    }

    @Override
    @Transactional
    public Project addProject(String name, String description, String status, String startDate, int size, List<Long> technologiesId) throws DuplicateInstanceException, InstanceNotFoundException {
        if(projectDao.existsByName(name)){
            throw new DuplicateInstanceException("project.entity.project", projectDao.findByName(name).get().getId());
        }
        Project project = new Project(name, description, status, startDate, size);
        projectDao.save(project);
        try{
            updateProjectTechnologies(project, technologiesId);
        } catch (InstanceNotFoundException e) {
            projectDao.delete(project);
            throw e;
        }
        return project;
    }

    @Override
    @Transactional
    public void updateProject(Long id, String name, String description, String status, String startDate, int size, Boolean updateTechnologies, List<Long> technologiesId) throws InstanceNotFoundException, DuplicateInstanceException {
        Project project = projectDao.findById(id).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", id));
        if(projectDao.existsByNameAndIdIsNot(name, id)){
            throw new DuplicateInstanceException("project.entity.project", projectDao.findByNameAndIdIsNot(name, id).get().getId());
        }
        if(name != null){
            project.setName(name);
        }
        if(description != null){
            project.setDescription(description);
        }
        if(status != null){
            project.setStatus(status);
        }
        if(startDate != null){
            project.setStartDate(startDate);
        }
        if(size > -1){
            project.setSize(size);
        }
        if(updateTechnologies){
            updateProjectTechnologies(project, technologiesId);
        }
        projectDao.save(project);
    }

    public void updateProjectTechnologies(Project project, List<Long> technologiesId) throws InstanceNotFoundException {

        List<Long> actualTechnologiesId = usesDao.findAllByProject(project).stream()
                .map(use -> use.getTechnology().getId())
                .collect(Collectors.toList());

        List<Long> newTechnologies = technologiesId.stream()
                .filter(id -> !actualTechnologiesId.contains(id))
                .collect(Collectors.toList());

        List<Long> missingTechnologies = actualTechnologiesId.stream()
                .filter(id -> !technologiesId.contains(id))
                .collect(Collectors.toList());

        for (Long technologyId : missingTechnologies) {
            Uses use = usesDao.findByProjectAndTechnologyId(project, technologyId);
            usesDao.delete(use);
        }

        for (Long technologyId : newTechnologies) {
            Technology technology = technologyDao.findById(technologyId).orElseThrow(() -> new InstanceNotFoundException("project.entity.technology", technologyId));
            Uses use = new Uses(project, technology);
            usesDao.save(use);
        }
    }

    @Override
    public void deleteProject(Long projectId) throws InstanceNotFoundException {
        Optional<Project> project =  projectDao.findById(projectId);
        if(!project.isPresent()){
            throw new InstanceNotFoundException("project.entities.technology", projectId);
        }
        projectDao.delete(project.get());
    }

    @Override
    public void addUses(Long projectId, Long technologyId) throws InstanceNotFoundException, DuplicateInstanceException {
        Project project = projectDao.findById(projectId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", projectId));
        Technology technology = technologyDao.findById(technologyId).orElseThrow(() -> new InstanceNotFoundException("project.entity.technology", technologyId));
        Optional<Uses> uses = usesDao.findByProjectAndTechnology(project, technology);
        if (uses.isPresent()) {
            throw new DuplicateInstanceException("project.entities.uses", uses.get().getId());
        }
        addUsesHierarchy(project, technology);
    }

    private List<Uses> addUsesHierarchy(Project project, Technology technology) {
        List<Uses> usesList = new ArrayList<>();
        if (technology.getParentId() != null) {
            List<Uses> brotherUses = usesDao.findAllByProjectAndTechnologyParentId(project, technology.getParentId());
            if (brotherUses.isEmpty()) {
                Optional<Technology> parentTechnology = technologyDao.findById(technology.getParentId());
                parentTechnology.ifPresent(value -> usesList.addAll(addUsesHierarchy(project, value)));
            }
        }
        if(!usesDao.existsByProjectAndTechnology(project, technology)){
            usesList.add(usesDao.save(new Uses(project, technology)));
        }
        return usesList;
    }

    @Override
    public Uses deleteUses(Long usesId) throws InstanceNotFoundException {
        Uses uses = usesDao.findById(usesId).orElseThrow(() -> new InstanceNotFoundException("project.entity.uses", usesId));
        deleteUsesHierarchy(uses);
        return uses;
    }

    private void deleteUsesHierarchy(Uses uses) {
        List<Uses> childrenUses = usesDao.findAllByProjectAndTechnologyParentId(uses.getProject(), uses.getTechnology().getId());
        usesDao.delete(uses);
        if (!childrenUses.isEmpty()) {
            for(Uses childUses : childrenUses){
                deleteUsesHierarchy(childUses);
            }
        }
    }

    @Override
    public Participation participate(Long userId, Long projectId, String startDate, String endDate) throws InstanceNotFoundException, DuplicateInstanceException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", userId));
        Project project = projectDao.findById(projectId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", projectId));
        Optional<Participation> participation = participationDao.findByUserAndProjectAndEndDateIsNull(user, project);
        if(participation.isPresent()){
            throw new DuplicateInstanceException("project.entity.participation", participation.get().getId());
        }
        return participationDao.save(new Participation(project, user, startDate, endDate));
    }

    @Override
    public Participation updateParticipate(Long participationId, String startDate, String endDate) throws InstanceNotFoundException {
        Participation participation = participationDao.findById(participationId).orElseThrow(() -> new InstanceNotFoundException("project.entity.participation", participationId));
        if(startDate != null) {
            participation.setStartDate(startDate);
        }
        if(endDate != null) {
            participation.setEndDate(endDate);
        }
        return participationDao.save(participation);
    }

    @Override
    public List<Verification> listVerification(Long userId, Long projectId) throws InstanceNotFoundException, InvalidAttributesException {
        if(userId != null) {
            User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", userId));
            if(projectId != null) {
                Project project = projectDao.findById(projectId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", projectId));
                return verificationDao.findAllByKnowledgeUserAndUsesProject(user, project);
            }
            return verificationDao.findAllByKnowledgeUser(user);
        }
        if(projectId != null) {
            Project project = projectDao.findById(projectId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", projectId));
            return verificationDao.findAllByUsesProject(project);
        }
        throw new InvalidAttributesException();
    }

    @Override
    public Verification addVerification(Long userId, Long usesId) throws InstanceNotFoundException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", userId));
        Uses uses = usesDao.findById(usesId).orElseThrow(() -> new InstanceNotFoundException("project.entity.uses", usesId));
        //crear la fila de verification y de knowledge si es necesario
        Knowledge knowledge = knowledgeDao.findByUserAndTechnology(user, uses.getTechnology()).orElse(null);
        if(knowledge == null) {
            try {
                knowledgeService.addKnowledge(userId, uses.getTechnology().getId(), null, null);
                knowledge = knowledgeDao.findByUserAndTechnology(user, uses.getTechnology()).orElse(null);
            } catch (InvalidAttributesException | DuplicateInstanceException ignored) {}
        }

        assert knowledge != null;
        return addVerificationHierarchy(uses, knowledge);
    }

    private Verification addVerificationHierarchy(Uses uses, Knowledge knowledge) {
        if (knowledge.getTechnology().getParentId() != null) {
            Optional<Verification> parentVerification = verificationDao.findByUsesProjectAndKnowledgeUserAndKnowledgeTechnologyId(uses.getProject(), knowledge.getUser(), knowledge.getTechnology().getParentId());
            if (!parentVerification.isPresent()) {
                addVerificationHierarchy(usesDao.findByProjectAndTechnologyId(uses.getProject(), uses.getTechnology().getParentId()),
                        knowledgeDao.findByUserAndTechnologyId(knowledge.getUser(),knowledge.getTechnology().getParentId()));
            }
        }
        return verificationDao.save(new Verification(knowledge, uses));
    }

    @Override
    public Verification deleteVerification(Long verificationId, Boolean deleteKnowledge) throws InstanceNotFoundException {
        Verification verification = verificationDao.findById(verificationId).orElseThrow(() -> new InstanceNotFoundException("project.entity.verification", verificationId));;

        return deleteVerificationHierarchy(verification, deleteKnowledge);
    }

    private Verification deleteVerificationHierarchy(Verification verification, Boolean deleteKnowledge) {
        List<Verification> childrenVerifications = verificationDao.findAllByUsesProjectAndKnowledgeUserAndKnowledgeTechnologyParentId(verification.getUses().getProject(), verification.getKnowledge().getUser(), verification.getKnowledge().getTechnology().getId());
        verificationDao.delete(verification);

        if(deleteKnowledge && !verificationDao.existsByKnowledgeUserAndKnowledgeTechnologyId(verification.getKnowledge().getUser(), verification.getKnowledge().getTechnology().getId())){
            knowledgeDao.delete(verification.getKnowledge());
        }

        if (!childrenVerifications.isEmpty()) {
            for(Verification childVerification : childrenVerifications){
                deleteVerificationHierarchy(childVerification, deleteKnowledge);
            }
        }
        return verification;
    }

}
