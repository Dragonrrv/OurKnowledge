package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.exceptions.*;
import com.example.ourknowledgebackend.model.TechnologiesTreeList;
import com.example.ourknowledgebackend.model.entities.Technology;

import java.util.List;

public interface TechnologyService {

    List<TechnologiesTreeList> listRelevantTechnologies();

    Technology addTechnology(String name, Long parentId, Long userId) throws InstanceNotFoundException, DuplicateInstanceException;

    void deleteTechnology(Long userId, Long technologyId, boolean deleteChildren) throws HaveChildrenException, PermissionException, InstanceNotFoundException;
}
