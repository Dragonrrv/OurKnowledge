package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.exceptions.PermissionException;
import com.example.ourknowledgebackend.model.entities.*;

public interface PermissionChecker {

    Long getUserIdByAuthentication();

    Technology checkTechnology(Long technologyId)
            throws InstanceNotFoundException;

    Technology checkTechnology(String name, Long parentId)
            throws InstanceNotFoundException;

    Knowledge checkKnowledgeExistsAndBelongsTo(Long knowledgeId, Long userId)
            throws InstanceNotFoundException, PermissionException;

    Filter checkFilterExistsAndBelongsTo(Long filterId, Long userId) throws InstanceNotFoundException, PermissionException;

    Knowledge checkKnowledge(User user, Technology technology)
            throws InstanceNotFoundException;

    Verification checkVerificationExistsAndBelongTo(Long verificationId, Long userId)
            throws InstanceNotFoundException, PermissionException;;

    Participation checkParticipationExistsAndBelongsTo(Long participationId, Long userId) throws InstanceNotFoundException, PermissionException;
}
