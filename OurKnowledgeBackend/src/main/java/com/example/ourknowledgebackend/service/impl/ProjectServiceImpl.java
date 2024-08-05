package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.exceptions.DuplicateInstanceException;
import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.model.KnowledgeTree;
import com.example.ourknowledgebackend.model.KnownTechnology;
import com.example.ourknowledgebackend.model.ProjectDetails;
import com.example.ourknowledgebackend.model.TechnologyTree;
import com.example.ourknowledgebackend.model.entities.*;
import com.example.ourknowledgebackend.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectDao projectDao;

    private final UsesDao usesDao;

    private final VerificationDao verificationDao;

    private final TechnologyDao technologyDao;

    private final Common common;

    @Override
    public List<Project> listProjects() {

        return (List<Project>) projectDao.findAll();
    }

    @Override
    public ProjectDetails projectDetails(Long id) throws InstanceNotFoundException {
        Project project = projectDao.findById(id).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", id));
        List<TechnologyTree> technologyTreeList = common.TechnologyListToTechnologyTreeList(
                usesDao.findAllByProject(project).stream().map(Uses::getTechnology).collect(Collectors.toList()));
        List<User> users = verificationDao.findAllByProject(project).stream().map(verification -> verification.getKnowledge().getUser()).collect(Collectors.toList());
        return new ProjectDetails(project, technologyTreeList, users);
    }

    @Override
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

    @Transactional
    public void updateProjectTechnologies(Long projectId, List<Long> technologiesId) throws InstanceNotFoundException {
        Project project = projectDao.findById(projectId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", projectId));

        usesDao.deleteByProjectId(projectId);

        for (Long technologyId : technologiesId) {
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


}
