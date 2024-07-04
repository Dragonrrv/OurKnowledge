package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.exceptions.DuplicateInstanceException;
import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.exceptions.PermissionException;
import com.example.ourknowledgebackend.model.entities.Knowledge;

import javax.naming.directory.InvalidAttributesException;
import java.util.List;

public interface KnowledgeService {

    List<Knowledge> addKnowledge(Long userId, Long technologyId, String technologyName, Long parentTechnologyId) throws InstanceNotFoundException, DuplicateInstanceException, InvalidAttributesException;

    void deleteKnowledge(Long userId, Long knowledgeId) throws InstanceNotFoundException, PermissionException;

    void updateKnowledge(Long userId, Long knowledgeId, Boolean mainSkill, Boolean likeIt) throws InstanceNotFoundException, PermissionException;
}
