package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.model.Tree;
import com.example.ourknowledgebackend.model.entities.Technology;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class Common {

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

}
