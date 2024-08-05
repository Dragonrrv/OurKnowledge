package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.exceptions.DuplicateInstanceException;
import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.model.ProjectDetails;
import com.example.ourknowledgebackend.model.entities.Project;

import java.util.List;

public interface ProjectService {

    List<Project> listProjects();

    ProjectDetails projectDetails(Long id) throws InstanceNotFoundException;

    void addProject(String name, String description, String status, String startDate, int size, List<Long> technologiesId) throws DuplicateInstanceException, InstanceNotFoundException;

    void deleteProject(Long projectId) throws InstanceNotFoundException;
}
