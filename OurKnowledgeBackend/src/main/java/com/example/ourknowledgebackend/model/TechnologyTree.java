package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Technology;
import java.util.ArrayList;

// the first technology is always null
public class TechnologyTree {

    Technology parentTechnology;

    ArrayList<TechnologyTree> childrenTechnology;

    public TechnologyTree() {

    }

    public TechnologyTree(Technology parentTechnology, ArrayList<TechnologyTree> childrenTechnology) {
        this.parentTechnology = parentTechnology;
        this.childrenTechnology = childrenTechnology;
    }

    public Technology getParentTechnology() {
        return parentTechnology;
    }

    public void setParentTechnology(Technology parentTechnology) {
        this.parentTechnology = parentTechnology;
    }

    public ArrayList<TechnologyTree> getChildrenTechnology() {
        return childrenTechnology;
    }

    public void setChildrenTechnology(ArrayList<TechnologyTree> childrenTechnology) {
        this.childrenTechnology = childrenTechnology;
    }
}
