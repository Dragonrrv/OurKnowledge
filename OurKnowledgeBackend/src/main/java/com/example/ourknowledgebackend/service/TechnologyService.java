package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.exceptions.*;
import com.example.ourknowledgebackend.model.TechnologyTree;

import java.util.List;

public interface TechnologyService {

    List<TechnologyTree> listRelevantTechnologies();

    List<TechnologyTree> addTechnology(String name, Long parentId, boolean relevant) throws InstanceNotFoundException, DuplicateInstanceException, PermissionException;

    List<TechnologyTree> deleteTechnology(Long technologyId, boolean deleteChildren) throws HaveChildrenException, PermissionException, InstanceNotFoundException;
}
