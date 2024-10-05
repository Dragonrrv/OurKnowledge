package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.exceptions.DuplicateInstanceException;
import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.model.ProjectDetails;
import com.example.ourknowledgebackend.model.ProjectResult;
import com.example.ourknowledgebackend.model.entities.Participation;
import com.example.ourknowledgebackend.model.entities.Project;
import com.example.ourknowledgebackend.model.entities.Uses;
import com.example.ourknowledgebackend.model.entities.Verification;

import javax.naming.directory.InvalidAttributesException;
import java.util.List;

public interface ProjectService {

    Block<ProjectResult> listProjects(int page, int size, String keywords, Long filterId) throws InstanceNotFoundException;

    ProjectDetails projectDetails(Long id) throws InstanceNotFoundException;

    Project addProject(String name, String description, String status, String startDate, int size, List<Long> technologiesId) throws DuplicateInstanceException, InstanceNotFoundException;

    void updateProject(Long id, String name, String description, String status, String startDate, int size, Boolean updateTechnologies, List<Long> technologiesId) throws InstanceNotFoundException, DuplicateInstanceException;

    void deleteProject(Long projectId) throws InstanceNotFoundException;

    void addUses(Long projectId, Long technologyId) throws InstanceNotFoundException, DuplicateInstanceException;

    Uses deleteUses(Long usesId) throws InstanceNotFoundException;

    Participation participate(Long userId, Long projectId, String startDate, String endDate) throws InstanceNotFoundException, DuplicateInstanceException;

    Participation updateParticipate(Long participationId, String startDate, String endDate) throws InstanceNotFoundException;

    List<Verification> listVerification(Long userId, Long project) throws InstanceNotFoundException, InvalidAttributesException;

    Verification addVerification(Long userId, Long usesId) throws InstanceNotFoundException;

    Verification deleteVerification(Long verificationId, Boolean deleteKnowledge) throws InstanceNotFoundException;
}
