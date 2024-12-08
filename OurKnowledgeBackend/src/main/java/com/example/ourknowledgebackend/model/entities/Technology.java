package com.example.ourknowledgebackend.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "Technology")
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long parentId;
    @JsonIgnore
    private boolean relevant;

    public Technology() {
    }

    public Technology(String name, Long parentId, boolean relevant) {
        this.name = name;
        this.parentId = parentId;
        this.relevant = relevant;
    }

    public Technology(Long id, String name, Long parentId, boolean relevant) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.relevant = relevant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public boolean isRelevant() {
        return relevant;
    }

    public void setRelevant(boolean relevant) {
        this.relevant = relevant;
    }
}
