package com.example.ourknowledgebackend.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Verification")
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "knowledgeId")
    private Knowledge knowledge;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    public Verification() {
    }

    public Verification(Knowledge knowledge, Project project) {
        this.knowledge = knowledge;
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Knowledge getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(Knowledge knowledge) {
        this.knowledge = knowledge;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
