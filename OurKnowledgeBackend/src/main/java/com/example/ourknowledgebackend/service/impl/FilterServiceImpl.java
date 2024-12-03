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
import org.springframework.transaction.annotation.Transactional;

import javax.naming.directory.InvalidAttributesException;
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

    private final ProjectDao projectDao;

    private final UsesDao usesDao;

    private final KnowledgeDao knowledgeDao;


    @Value("${app.constants.default_filter_name}")
    private String defaultFilter;

    @Override
    public List<Filter> listFilter(Long userId) throws InstanceNotFoundException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.user", userId));
        return filterDao.findByUserAndNameNot(user, defaultFilter);
    }

    @Override
    public CompleteFilter getFilter(Long userId, Long filterId) throws InstanceNotFoundException, PermissionException {
        Filter filter = permissionChecker.checkFilterExistsAndBelongsTo(filterId, userId);
        List<FilterParamTechnology> filterParamTechnologyList = extendedTechnologyDao.findTechnologiesWithFilter(filter.getId());
        List<FilterParamTree> filterParamTreeList = common.ListToTreeList(filterParamTechnologyList, FilterParamTree::new);
        return new CompleteFilter(filter, filterParamTreeList);
    }

    @Override
    public CompleteFilter getDefaultFilter(Long userId) throws InstanceNotFoundException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.user", userId));
        Filter filter = filterDao.findByUserAndName(user, defaultFilter).orElseThrow(() -> new InstanceNotFoundException("project.entity.filter", null));
        try {
            return getFilter(userId, filter.getId());
        } catch (PermissionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
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
    public void clearFilter(Long userId) throws InstanceNotFoundException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.user", userId));
        Filter filter = filterDao.findByUserAndName(user, defaultFilter).orElseThrow(() -> new InstanceNotFoundException("project.entity.filter", null));
        List<FilterParam> filterParamList = filterParamDao.findAllByFilter(filter);
        filterParamDao.deleteAll(filterParamList);
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

    @Override
    public void createByProject(Long userId, Long projectId) throws InstanceNotFoundException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.user", userId));
        Project project = projectDao.findById(projectId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", projectId));
        Filter filter = filterDao.findByUserAndName(user, defaultFilter).orElse(null);
        List<FilterParam> filterParamList = usesDao.findAllByProject(project).stream()
                .map( uses -> new FilterParam( filter, uses.getTechnology(), false, true ) ).collect(Collectors.toList());
        clearFilter(userId);
        filterParamDao.saveAll(filterParamList);
    }

    @Override
    public void createByUser(Long userId, Long userAsFilterId) throws InstanceNotFoundException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.user", userId));
        User userAsFilter = userDao.findById(userAsFilterId).orElseThrow(() -> new InstanceNotFoundException("project.entity.user", userAsFilterId));
        Filter filter = filterDao.findByUserAndName(user, defaultFilter).orElse(null);
        List<FilterParam> filterParamList = knowledgeDao.findAllByUser(userAsFilter).stream()
                .map( knowledge -> new FilterParam( filter, knowledge.getTechnology(), false, true ) ).collect(Collectors.toList());
        clearFilter(userId);
        filterParamDao.saveAll(filterParamList);
    }

    private void addFilterParam(Filter filter, Long technologyId, Boolean mandatory, Boolean recommended) throws InstanceNotFoundException, PermissionException, InvalidAttributesException, DuplicateInstanceException {
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
            if (mandatory && brotherFilterParams.stream().noneMatch(FilterParam::isMandatory)) {
                FilterParam filterParamParent = filterParamDao.findByFilterAndTechnologyId(filter, technology.getParentId()).orElse(null);
                updateFilterParamHierarchy(filterParamParent, true, false);
            }
        }
        if(!filterParamDao.existsByFilterAndTechnology(filter, technology)){
            filterParamDao.save(new FilterParam(filter, technology, mandatory, recommended));
        }
    }

    private void deleteFilterParam(Long filterParamId) throws InstanceNotFoundException {
        FilterParam filterParam = filterParamDao.findById(filterParamId).orElseThrow(() -> new InstanceNotFoundException("project.entity.filterParam", filterParamId));
        if (filterParam.isMandatory()) {
            FilterParamTechnology filterParamTechnology = extendedTechnologyDao.findFilterParamTechnology(filterParamId);
            if (filterParamTechnology.getRecommendedUnnecessary()){
                updateFilterParam(filterParamId, false, true);
                return;
            }
            List<FilterParam> filterParamBrothers = filterParamDao.findAllByFilterAndTechnologyParentId(filterParam.getFilter(), filterParam.getTechnology().getParentId());
            filterParamBrothers.remove(filterParam);
            if (!filterParamBrothers.isEmpty() && filterParamBrothers.stream().noneMatch(FilterParam::isMandatory)) {
                filterParamDao.delete(filterParam);
                updateFilterParam(filterParamDao.findByFilterAndTechnologyId(filterParam.getFilter(), filterParam.getTechnology().getParentId()).orElse(null).getId(), false, true);
                return;
            }
        }
        deleteFilterParamHierarchy(filterParam, filterParam.isRecommended());
    }

    private void deleteFilterParamHierarchy(FilterParam filterParam, Boolean recommended) {
        if (filterParam.getTechnology().getParentId() != null) {
            List<FilterParam> brotherFilterParams = filterParamDao.findAllByFilterAndTechnologyParentId(
                    filterParam.getFilter() , filterParam.getTechnology().getParentId());
            if (brotherFilterParams.size() == 1) {
                Optional<FilterParam> parentFilterParam = filterParamDao.findByFilterAndTechnologyId(filterParam.getFilter(), filterParam.getTechnology().getParentId());
                parentFilterParam.ifPresent(value -> deleteFilterParamHierarchy(value, recommended));
            }
        }
        if(recommended){
            if(filterParam.isMandatory()){
                return;
            }
        }
        filterParamDao.delete(filterParam);
    }

    private void updateFilterParam(Long filterParamId, Boolean mandatory, Boolean recommended) throws InstanceNotFoundException {
        FilterParam filterParam = filterParamDao.findById(filterParamId).orElseThrow(() -> new InstanceNotFoundException("project.entity.filterParam", filterParamId));
        updateFilterParamHierarchy(filterParam, mandatory, recommended);
    }

    private void updateFilterParamHierarchy(FilterParam filterParam, Boolean mandatory, Boolean recommended) {
        if (filterParam.getTechnology().getParentId() != null) {
            List<FilterParam> brotherFilterParams = filterParamDao.findAllByFilterAndTechnologyParentId(filterParam.getFilter(), filterParam.getTechnology().getParentId());
            brotherFilterParams.remove(filterParam);

            if (brotherFilterParams.isEmpty()) {
                FilterParam filterParamParent = filterParamDao.findByFilterAndTechnologyId(filterParam.getFilter(), filterParam.getTechnology().getParentId()).orElse(null);
                updateFilterParamHierarchy(filterParamParent, mandatory, recommended);
            }
        }
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
