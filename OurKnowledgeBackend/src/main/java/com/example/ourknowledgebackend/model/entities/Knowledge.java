package com.example.ourknowledgebackend.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Knowledge")
public class Knowledge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "technologyId")
    private Technology technology;

    private boolean mainSkill;
    private boolean likeIt;

    public Knowledge() {}

    public Knowledge(User user, Technology technology, boolean mainSkill, boolean likeIt) {
        this.user = user;
        this.technology = technology;
        this.mainSkill = mainSkill;
        this.likeIt = likeIt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Technology getTechnology() {
        return technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }

    public boolean isMainSkill() {
        return mainSkill;
    }

    public void setMainSkill(boolean mainSkill) {
        this.mainSkill = mainSkill;
    }

    public boolean isLikeIt() {
        return likeIt;
    }

    public void setLikeIt(boolean likeIt) {
        this.likeIt = likeIt;
    }
}
