package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.exceptions.DuplicateInstanceException;
import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.model.ProjectDetails;
import com.example.ourknowledgebackend.model.entities.Project;
import com.example.ourknowledgebackend.model.entities.Uses;

import java.util.List;

public interface ProjectService {

    Block<Project> listProjects(int page, String keywords, int size);

    ProjectDetails projectDetails(Long id) throws InstanceNotFoundException;

    Project addProject(String name, String description, String status, String startDate, int size, List<Long> technologiesId) throws DuplicateInstanceException, InstanceNotFoundException;

    void updateProject(Long id, String name, String description, String status, String startDate, int size, Boolean updateTechnologies, List<Long> technologiesId) throws InstanceNotFoundException, DuplicateInstanceException;

    void deleteProject(Long projectId) throws InstanceNotFoundException;

    void addUses(Long projectId, Long technologyId) throws InstanceNotFoundException, DuplicateInstanceException;

    Uses deleteUses(Long usesId) throws InstanceNotFoundException;

    void participate(Long userId, Long projectId, String startDate, String endDate) throws InstanceNotFoundException, DuplicateInstanceException;

    void updateParticipate(Long participationId, String startDate, String endDate) throws InstanceNotFoundException;

    void addVerification(Long userId, Long usesId) throws InstanceNotFoundException;

    void deleteVerification(Long userId, Long usesId, Boolean deleteKnowledge) throws InstanceNotFoundException;
}
