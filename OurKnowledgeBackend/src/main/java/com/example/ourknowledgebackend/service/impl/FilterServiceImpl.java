package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.exceptions.DuplicateInstanceException;
import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.exceptions.PermissionException;
import com.example.ourknowledgebackend.model.*;
import com.example.ourknowledgebackend.model.entities.*;
import com.example.ourknowledgebackend.service.FilterService;
import com.example.ourknowledgebackend.service.PermissionChecker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.directory.InvalidAttributesException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements FilterService {

    @PersistenceContext
    private EntityManager entityManager;

    private final PermissionChecker permissionChecker;

    private final Common common;

    private final FilterDao filterDao;

    private final FilterParamDao filterParamDao;

    private final ExtendedTechnologyDao extendedTechnologyDao;

    private final UserDao userDao;

    private final TechnologyDao technologyDao;


    @Value("${app.constants.default_filter_name}")
    private String defaultFilter;

    @Override
    public List<Filter> listFilter(Long userId) throws InstanceNotFoundException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.user", userId));
        return filterDao.findByUserAndNameNot(user, defaultFilter);
    }

    @Override
    public CompleteFilter getFilter(Long filterId) throws InstanceNotFoundException {
        Filter filter = filterDao.findById(filterId).orElseThrow(() -> new InstanceNotFoundException("project.entity.filter", filterId));
        List<FilterParamTechnology> filterParamTechnologyList = extendedTechnologyDao.findTechnologiesWithFilter(filter.getId());
        List<FilterParamTree> filterParamTreeList = common.ListToTreeList(filterParamTechnologyList, FilterParamTree::new);
        return new CompleteFilter(filter, filterParamTreeList);
    }

    @Override
    public CompleteFilter getDefaultFilter(Long userId) throws InstanceNotFoundException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.user", userId));
        Filter filter = filterDao.findByUserAndName(user, defaultFilter).orElseThrow(() -> new InstanceNotFoundException("project.entity.filter", null));
        return getFilter(filter.getId());
    }

    @Override
    public void saveFilter(Long userId, String name) throws InstanceNotFoundException, DuplicateInstanceException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.user", userId));
        Filter filter = filterDao.findByUserAndName(user, defaultFilter).orElseThrow(() -> new InstanceNotFoundException("project.entity.filter", null));
        if(filterDao.findByName(name).isPresent()){
            throw new DuplicateInstanceException("project.entity.filter", null);
        }
        filter.setName(name);
        filterDao.save(filter);
        entityManager.flush();
        filterDao.save(new Filter(user, defaultFilter));
    }

    @Override
    public void deleteFilter(Long userId, Long filterId) throws InstanceNotFoundException, PermissionException {
        Filter filter = permissionChecker.checkFilterExistsAndBelongsTo(filterId, userId);
        if(filter.getName().equals(defaultFilter)) {
            filterParamDao.deleteAll(filterParamDao.findAllByFilter(filter));
        } else {
            filterDao.delete(filter);
        }
    }

    public void addFilterParam(Filter filter, Long technologyId, Boolean mandatory, Boolean recommended) throws InstanceNotFoundException, PermissionException, InvalidAttributesException, DuplicateInstanceException {
        Technology technology = technologyDao.findById(technologyId).orElseThrow(() -> new InstanceNotFoundException("project.entity.technology", technologyId));
        if(mandatory && recommended || !(mandatory || recommended)){
            throw new InvalidAttributesException();
        }
        if(filterParamDao.existsByFilterAndTechnology(filter, technology)) {
            throw new DuplicateInstanceException("project.entity.filterParam", null);
        }
        // anadir el mismo filterParam para todas las tecnologías padres
        addFilterParamHierarchy(filter, technology, mandatory, recommended);
    }


    private void addFilterParamHierarchy(Filter filter, Technology technology, Boolean mandatory, Boolean recommended) {
        if (technology.getParentId() != null) {
            List<FilterParam> brotherFilterParams = filterParamDao.findAllByFilterAndTechnologyParentId(filter, technology.getParentId());
            if (brotherFilterParams.isEmpty()) {
                Optional<Technology> parentTechnology = technologyDao.findById(technology.getParentId());
                parentTechnology.ifPresent(value -> addFilterParamHierarchy(filter, value, mandatory, recommended));
            }
        }
        if(!filterParamDao.existsByFilterAndTechnology(filter, technology)){
            filterParamDao.save(new FilterParam(filter, technology, mandatory, recommended));
        }
    }

    public void deleteFilterParam(Long filterParamId) throws InstanceNotFoundException {
        FilterParam filterParam = filterParamDao.findById(filterParamId).orElseThrow(() -> new InstanceNotFoundException("project.entity.filterParam", filterParamId));
        // Borrar todos los filterParam padres (aunque parezca extraño) y todos los hijos (aunque debiera ser inecesario)
        deleteFilterParamHierarchy(filterParam);
    }

    private void deleteFilterParamHierarchy(FilterParam filterParam) {
        if (filterParam.getTechnology().getParentId() != null) {
            List<FilterParam> brotherFilterParams = filterParamDao.findAllByFilterAndTechnologyParentId(
                    filterParam.getFilter() , filterParam.getTechnology().getParentId());
            if (brotherFilterParams.size() == 1) {
                Optional<FilterParam> parentFilterParam = filterParamDao.findByFilterAndTechnologyId(filterParam.getFilter(), filterParam.getTechnology().getParentId());
                parentFilterParam.ifPresent(this::deleteFilterParamHierarchy);
            }
        }
        filterParamDao.delete(filterParam);
    }

    public void updateFilterParam(Long filterParamId, Boolean mandatory, Boolean recommended) throws InstanceNotFoundException {
        FilterParam filterParam = filterParamDao.findById(filterParamId).orElseThrow(() -> new InstanceNotFoundException("project.entity.filterParam", filterParamId));
        filterParam.setMandatory(mandatory);
        filterParam.setRecommended(recommended);
        filterParamDao.save(filterParam);
    }

    @Override
    public Long updateFilterParam(Long userId, Long filterParamId, Long filterId, Long technologyId, Boolean mandatory, Boolean recommended) throws InstanceNotFoundException, InvalidAttributesException, PermissionException, DuplicateInstanceException {
        if (filterParamId == null) {
            if(filterId == null || technologyId == null) {
                throw new InvalidAttributesException();
            }
            Filter filter = permissionChecker.checkFilterExistsAndBelongsTo(filterId, userId);
            filterId = filter.getId();
            addFilterParam(filter, technologyId, mandatory, recommended);
        } else {
            if(mandatory && recommended){
                throw new InvalidAttributesException();
            }
            if (!mandatory && !recommended) {
                deleteFilterParam(filterParamId);
            } else {
                updateFilterParam(filterParamId, mandatory, recommended);
            }
        }
        return filterId;
    }
}
