package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.exceptions.DuplicateInstanceException;
import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.model.ProjectDetails;
import com.example.ourknowledgebackend.model.TechnologyTree;
import com.example.ourknowledgebackend.model.entities.*;
import com.example.ourknowledgebackend.service.Block;
import com.example.ourknowledgebackend.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectDao projectDao;

    private final UsesDao usesDao;

    private final ParticipationDao participationDao;

    private final VerificationDao verificationDao;

    private final TechnologyDao technologyDao;

    private final UserDao userDao;

    private final KnowledgeDao knowledgeDao;

    private final Common common;

    @Override
    public Block<Project> listProjects(int page, String keywords, int size) {
        Slice<Project> slice = projectDao.find(page, keywords, size);

        return new Block<>(slice.getContent(), slice.hasNext());
    }

    @Override
    public ProjectDetails projectDetails(Long id) throws InstanceNotFoundException {
        Project project = projectDao.findById(id).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", id));
        List<TechnologyTree> technologyTreeList = common.TechnologyListToTechnologyTreeList(
                usesDao.findAllByProject(project).stream().map(Uses::getTechnology).collect(Collectors.toList()));
        List<User> users = participationDao.findAllByProject(project).stream().map(Participation::getUser).collect(Collectors.toList());
        return new ProjectDetails(project, technologyTreeList, users);
    }

    @Override
    @Transactional
    public void addProject(String name, String description, String status, String startDate, int size, List<Long> technologiesId) throws DuplicateInstanceException, InstanceNotFoundException {
        if(projectDao.existsByName(name)){
            throw new DuplicateInstanceException("project.entity.project", projectDao.findByName(name).get().getId());
        }
        Project project = new Project(name, description, status, startDate, size);
        projectDao.save(project);
        try{
            updateProjectTechnologies(project.getId(), technologiesId);
        } catch (InstanceNotFoundException e) {
            projectDao.delete(project);
            throw e;
        }
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
            updateProjectTechnologies(id, technologiesId);
        }
        projectDao.save(project);
    }

    public void updateProjectTechnologies(Long projectId, List<Long> technologiesId) throws InstanceNotFoundException {
        Project project = projectDao.findById(projectId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", projectId));

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
    public void participate(Long userId, Long projectId, String startDate, String endDate) throws InstanceNotFoundException, DuplicateInstanceException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", userId));
        Project project = projectDao.findById(projectId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", projectId));
        Optional<Participation> participation = participationDao.findByUserAndProjectAndEndDateIsNull(user, project);
        if(participation.isPresent()){
            throw new DuplicateInstanceException("project.entity.participation", participation.get().getId());
        }
        participationDao.save(new Participation(project, user, startDate, endDate));
    }

    @Override
    public void updateParticipate(Long participationId, String startDate, String endDate) throws InstanceNotFoundException {
        Participation participation = participationDao.findById(participationId).orElseThrow(() -> new InstanceNotFoundException("project.entity.participation", participationId));
        if(startDate != null) {
            participation.setStartDate(startDate);
        }
        if(endDate != null) {
            participation.setEndDate(endDate);
        }
        participationDao.save(participation);
    }

    @Override
    public void verificate(Long userId, Long projectId, List<Long> technologiesId) throws InstanceNotFoundException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", userId));
        Project project = projectDao.findById(projectId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", projectId));
        //comprobar que el proyecto usa esas tecnologías
        if(!new HashSet<>(usesDao.findAllByProject(project).stream().map(use -> use.getTechnology().getId()).collect(Collectors.toList()))
                .containsAll(technologiesId)){
            throw new InstanceNotFoundException("project.entity.uses", null);
        }
        //crear todas las filas de verification y de knowledge si es necesario
        for (Long technologyId : technologiesId) {
            Technology technology = technologyDao.findById(technologyId).get();
            Optional<Knowledge> knowledge = knowledgeDao.findByUserAndTechnology(user, technology);
            if(knowledge.isPresent()){
                verificationDao.save(new Verification(knowledge.get(), project));
            }else{
                Knowledge knowledge1 = knowledgeDao.save(new Knowledge(user, technology, false, false));
                verificationDao.save(new Verification(knowledge1, project));
            }
        }
    }


}
