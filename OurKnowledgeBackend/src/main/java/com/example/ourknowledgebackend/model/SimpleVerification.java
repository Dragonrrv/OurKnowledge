package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Verification;

public class SimpleVerification {

    private Long id;

    private Long userId;

    private Long projectId;

    private String userEmail;

    private String projectName;

    public SimpleVerification(Verification verification) {
        this.id = verification.getId();
        this.userId = verification.getKnowledge().getUser().getId();
        this.projectId = verification.getUses().getProject().getId();
        this.userEmail =  verification.getKnowledge().getUser().getEmail();
        this.projectName = verification.getUses().getProject().getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
