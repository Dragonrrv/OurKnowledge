package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.exceptions.DuplicateInstanceException;
import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.exceptions.PermissionException;
import com.example.ourknowledgebackend.model.CompleteFilter;
import com.example.ourknowledgebackend.model.entities.Filter;

import javax.naming.directory.InvalidAttributesException;
import java.util.List;

public interface FilterService {

    List<Filter> listFilter(Long userId) throws InstanceNotFoundException;

    CompleteFilter getFilter(Long filterId) throws InstanceNotFoundException;

    CompleteFilter getDefaultFilter(Long userId) throws InstanceNotFoundException;

    void saveFilter(Long userId, String name) throws InstanceNotFoundException, DuplicateInstanceException;

    void clearFilter(Long userId) throws InstanceNotFoundException;

    void deleteFilter(Long userId, Long filterId) throws InstanceNotFoundException, PermissionException;

    void createByProject(Long userId, Long projectId) throws InstanceNotFoundException;

    Long updateFilterParam(Long userId, Long filterParamId, Long filterId, Long technologyId, Boolean mandatory, Boolean recommended) throws InstanceNotFoundException, InvalidAttributesException, PermissionException, DuplicateInstanceException;

}
