package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Knowledge;
import com.example.ourknowledgebackend.model.entities.Technology;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeTree extends Tree<KnownTechnology>{

    public KnowledgeTree(KnownTechnology parent, List<KnowledgeTree> children) {
        super(parent, children);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<KnowledgeTree> getChildren() {
        return (List<KnowledgeTree>) super.getChildren();
    }
}
