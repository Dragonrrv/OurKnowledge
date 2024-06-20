package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Technology;
import java.util.ArrayList;

// the first technology is always null
public class TechnologiesTreeList {

    Technology parentTechnology;

    ArrayList<TechnologiesTreeList> childTechnologies;

    public TechnologiesTreeList() {

    }

    public TechnologiesTreeList(Technology parentTechnology, ArrayList<TechnologiesTreeList> childTechnologies) {
        this.parentTechnology = parentTechnology;
        this.childTechnologies = childTechnologies;
    }

    public Technology getParentTechnology() {
        return parentTechnology;
    }

    public void setParentTechnology(Technology parentTechnology) {
        this.parentTechnology = parentTechnology;
    }

    public ArrayList<TechnologiesTreeList> getChildTechnologies() {
        return childTechnologies;
    }

    public void setChildTechnologies(ArrayList<TechnologiesTreeList> childTechnologies) {
        this.childTechnologies = childTechnologies;
    }
}
