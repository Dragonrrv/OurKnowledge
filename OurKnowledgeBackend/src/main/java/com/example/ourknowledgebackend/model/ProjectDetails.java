package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Project;
import com.example.ourknowledgebackend.model.entities.User;

import java.util.List;

public class ProjectDetails {

    Project project;

    List<TechnologyTree> TechnologyTreeList;

    List<User> UserList;

    public ProjectDetails() {
    }

    public ProjectDetails(Project project, List<TechnologyTree> technologyTreeList, List<User> userList) {
        this.project = project;
        TechnologyTreeList = technologyTreeList;
        UserList = userList;
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

    public List<User> getUserList() {
        return UserList;
    }

    public void setUserList(List<User> userList) {
        UserList = userList;
    }
}
