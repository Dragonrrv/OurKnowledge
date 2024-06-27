package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.exceptions.*;
import com.example.ourknowledgebackend.model.TechnologiesTreeList;
import com.example.ourknowledgebackend.model.entities.Technology;

import java.util.List;

public interface TechnologyService {

    List<TechnologiesTreeList> listRelevantTechnologies();

    List<TechnologiesTreeList> addTechnology(String name, Long parentId, Long userId) throws InstanceNotFoundException, DuplicateInstanceException;

    List<TechnologiesTreeList> deleteTechnology(Long userId, Long technologyId, boolean deleteChildren) throws HaveChildrenException, PermissionException, InstanceNotFoundException;
}
