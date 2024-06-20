package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.exceptions.PermissionException;
import com.example.ourknowledgebackend.model.entities.Knowledge;
import com.example.ourknowledgebackend.model.entities.Technology;
import com.example.ourknowledgebackend.model.entities.User;

public interface PermissionChecker {

    User checkUser(Long userId) throws InstanceNotFoundException;

    Technology checkTechnology(Long technologyId)
            throws InstanceNotFoundException;

    Technology checkTechnology(String name, Long parentId)
            throws InstanceNotFoundException;

    Knowledge checkKnowledgeExistsAndBelongsTo(Long knowledgeId, Long userId)
            throws InstanceNotFoundException, PermissionException;

    Knowledge checkKnowledge(User user, Technology technology)
            throws InstanceNotFoundException;
}
