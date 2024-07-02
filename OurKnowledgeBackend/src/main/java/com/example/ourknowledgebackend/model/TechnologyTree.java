package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Technology;
import java.util.List;
import java.util.stream.Collectors;

public class TechnologyTree extends Tree<Technology> {

    public TechnologyTree(Technology parent, List<TechnologyTree> children) {
        super(parent, children);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TechnologyTree> getChildren() {
        return (List<TechnologyTree>) super.getChildren();
    }
}
