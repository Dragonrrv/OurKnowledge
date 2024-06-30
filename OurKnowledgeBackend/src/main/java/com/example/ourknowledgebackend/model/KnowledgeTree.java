package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Knowledge;

import java.util.ArrayList;

// the first Knowledge is always null
public class KnowledgeTree {

    Knowledge parentKnowledge;

    ArrayList<KnowledgeTree> childrenKnowledge;

    public KnowledgeTree() {

    }

    public KnowledgeTree(Knowledge parentKnowledge, ArrayList<KnowledgeTree> childrenKnowledge) {
        this.parentKnowledge = parentKnowledge;
        this.childrenKnowledge = childrenKnowledge;
    }

    public Knowledge getParentKnowledge() {
        return parentKnowledge;
    }

    public void setParentKnowledge(Knowledge parentKnowledge) {
        this.parentKnowledge = parentKnowledge;
    }

    public ArrayList<KnowledgeTree> getChildrenKnowledge() {
        return childrenKnowledge;
    }

    public void setChildrenKnowledge(ArrayList<KnowledgeTree> childrenKnowledge) {
        this.childrenKnowledge = childrenKnowledge;
    }
}
