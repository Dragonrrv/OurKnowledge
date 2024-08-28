package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Participation;
import com.example.ourknowledgebackend.model.entities.Project;

import java.util.List;

public class ProjectDetails {

    Project project;

    List<TechnologyTree> TechnologyTreeList;

    List<Participation> participationList;

    public ProjectDetails() {
    }

    public ProjectDetails(Project project, List<TechnologyTree> technologyTreeList, List<Participation> participationList) {
        this.project = project;
        TechnologyTreeList = technologyTreeList;
        this.participationList = participationList;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<TechnologyTree> getTechnologyTreeList() {
        return TechnologyTreeList;
    }

    public void setTechnologyTreeList(List<TechnologyTree> technologyTreeList) {
        TechnologyTreeList = technologyTreeList;
    }

    public List<Participation> getParticipationList() {
        return participationList;
    }

    public void setParticipationList(List<Participation> participationList) {
        this.participationList = participationList;
    }
}
