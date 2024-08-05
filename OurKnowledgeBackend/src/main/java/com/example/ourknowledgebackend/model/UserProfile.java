package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Project;
import com.example.ourknowledgebackend.model.entities.User;

import java.util.List;

public class UserProfile {
    User user;

    List<Project> projects;

    List<KnowledgeTree> knowledgeTreeList;

    public UserProfile() {
    }

    public UserProfile(User user, List<Project> projects, List<KnowledgeTree> knowledgeTreeList) {
        user.setRole(null);
        this.user = user;
        this.projects = projects;
        this.knowledgeTreeList = knowledgeTreeList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<KnowledgeTree> getKnowledgeTreeList() {
        return knowledgeTreeList;
    }

    public void setKnowledgeTreeList(List<KnowledgeTree> knowledgeTreeList) {
        this.knowledgeTreeList = knowledgeTreeList;
    }
}
