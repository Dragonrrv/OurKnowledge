package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.model.TechnologyTree;
import com.example.ourknowledgebackend.model.Tree;
import com.example.ourknowledgebackend.model.entities.Technology;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class Common {

    protected List<? extends Tree<? extends Technology>> ListToTreeList(List<? extends Technology> technologies) {
        Map<Long, List<Technology>> technologiesMap = technologies.stream()
                .collect(Collectors.groupingBy(tech -> tech.getParentId() != null ? tech.getParentId() : 0L));

        TechnologyTree root = fillTreeList(null, technologiesMap, 0L);

        return root.getChildren();
    }

    private TechnologyTree fillTreeList(Technology parent, Map<Long, List<Technology>> technologiesMap, Long parentId) {
        List<Technology> childTechnologies = technologiesMap.get(parentId);
        List<TechnologyTree> childTreeList = new ArrayList<>();
        if (childTechnologies != null) {
            for (Technology technology : childTechnologies) {
                childTreeList.add(fillTreeList(technology, technologiesMap, technology.getId()));
            }
        }
        return new TechnologyTree(parent, childTreeList);
    }

}
