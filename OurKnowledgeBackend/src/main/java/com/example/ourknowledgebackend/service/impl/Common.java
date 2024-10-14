package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.model.FilterParamTechnology;
import com.example.ourknowledgebackend.model.Tree;
import com.example.ourknowledgebackend.model.entities.ExtendedTechnologyDao;
import com.example.ourknowledgebackend.model.entities.Filter;
import com.example.ourknowledgebackend.model.entities.Technology;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Common {

    private final ExtendedTechnologyDao extendedTechnologyDao;

    protected <T extends Technology, R extends Tree<T>> List<R> ListToTreeList(List<T> technologies, BiFunction<T, List<R>, R> treeFactory) {

        Map<Long, List<T>> technologiesMap = technologies.stream()
                .collect(Collectors.groupingBy(tech -> tech.getParentId() != null ? tech.getParentId() : 0L));

        return (List<R>) fillTreeList(null, technologiesMap, 0L, treeFactory).getChildren();

    }

    private <T extends Technology, R extends Tree<T>> R fillTreeList(T parent, Map<Long, List<T>> technologiesMap, Long parentId, BiFunction<T, List<R>, R> treeFactory) {
        List<T> childTechnologies = technologiesMap.get(parentId);
        List<R> childTreeList = new ArrayList<>();
        if (childTechnologies != null) {
            for (T technology : childTechnologies) {
                childTreeList.add(fillTreeList(technology, technologiesMap, technology.getId(), treeFactory));
            }
        }
        return treeFactory.apply(parent, childTreeList);
    }

    /**
     * @return return a Map<String, List<Long> with 2 lists whose keys are "mandatory" and "recommended">
     */
    protected Map<String, List<Long>> getFilterParamTechnologiesId(Filter filter){

        List<FilterParamTechnology> filterParamTechnologyList = extendedTechnologyDao.findTechnologiesWithFilter(filter.getId());

        List<Long> mandatoryList = filterParamTechnologyList.stream()
                .filter(filterParamTechnology -> filterParamTechnology.getFilterParamId() != null && !filterParamTechnology.getUnnecessary() && filterParamTechnology.getMandatory())
                .map(Technology::getId)
                .collect(Collectors.toList());

        List<Long> recommendedList = filterParamTechnologyList.stream()
                .filter(filterParamTechnology -> filterParamTechnology.getFilterParamId() != null && !filterParamTechnology.getRecommendedUnnecessary() && filterParamTechnology.getRecommended())
                .map(Technology::getId)
                .collect(Collectors.toList());

        Map<String, List<Long>> result = new HashMap<>();
        result.put("mandatory", mandatoryList);
        result.put("recommended", recommendedList);
        return result;
    }

}
