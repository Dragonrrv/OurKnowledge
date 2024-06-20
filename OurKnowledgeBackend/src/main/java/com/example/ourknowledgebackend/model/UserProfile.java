package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.User;

public class UserProfile {
    User user;

    //ArrayList<Project>

    KnowledgeTreeList knowledgeTreeList;

    public UserProfile() {
    }

    public UserProfile(User user, KnowledgeTreeList knowledgeTreeList) {
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

    public KnowledgeTreeList getKnowledgeTreeList() {
        return knowledgeTreeList;
    }

    public void setKnowledgeTreeList(KnowledgeTreeList knowledgeTreeList) {
        this.knowledgeTreeList = knowledgeTreeList;
    }
}
