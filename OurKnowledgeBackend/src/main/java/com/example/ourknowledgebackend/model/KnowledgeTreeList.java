package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Knowledge;

import java.util.ArrayList;

// the first Knowledge is always null
public class KnowledgeTreeList {

    Knowledge parentKnowledge;

    ArrayList<KnowledgeTreeList> childKnowledges;

    public KnowledgeTreeList() {

    }

    public KnowledgeTreeList(Knowledge parentKnowledge, ArrayList<KnowledgeTreeList> childKnowledges) {
        this.parentKnowledge = parentKnowledge;
        this.childKnowledges = childKnowledges;
    }

    public Knowledge getParentKnowledge() {
        return parentKnowledge;
    }

    public void setParentKnowledge(Knowledge parentKnowledge) {
        this.parentKnowledge = parentKnowledge;
    }

    public ArrayList<KnowledgeTreeList> getChildKnowledges() {
        return childKnowledges;
    }

    public void setChildKnowledges(ArrayList<KnowledgeTreeList> childKnowledges) {
        this.childKnowledges = childKnowledges;
    }
}
