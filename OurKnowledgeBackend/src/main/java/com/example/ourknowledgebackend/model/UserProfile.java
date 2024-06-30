package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.User;

import java.util.List;

public class UserProfile {
    User user;

    //ArrayList<Project>

    List<KnowledgeTree> knowledgeTreeList;

    public UserProfile() {
    }

    public UserProfile(User user, List<KnowledgeTree> knowledgeTreeList) {
        user.setPassword(null);
        user.setRole(null);
        this.user = user;
        this.knowledgeTreeList = knowledgeTreeList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<KnowledgeTree> getKnowledgeTreeList() {
        return knowledgeTreeList;
    }

    public void setKnowledgeTreeList(List<KnowledgeTree> knowledgeTreeList) {
        this.knowledgeTreeList = knowledgeTreeList;
    }
}
