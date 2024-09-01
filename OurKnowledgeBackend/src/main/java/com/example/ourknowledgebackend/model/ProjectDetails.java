package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Participation;
import com.example.ourknowledgebackend.model.entities.Project;

import java.util.List;

public class ProjectDetails {

    Project project;

    List<UsesTree> usesTreeList;

    List<Participation> participationList;

    public ProjectDetails() {
    }

    public ProjectDetails(Project project, List<UsesTree> usesTreeList, List<Participation> participationList) {
        this.project = project;
        this.usesTreeList = usesTreeList;
        this.participationList = participationList;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<UsesTree> getUsesTreeList() {
        return usesTreeList;
    }

    public void setUsesTreeList(List<UsesTree> usesTreeList) {
        this.usesTreeList = usesTreeList;
    }

    public List<Participation> getParticipationList() {
        return participationList;
    }

    public void setParticipationList(List<Participation> participationList) {
        this.participationList = participationList;
    }
}
