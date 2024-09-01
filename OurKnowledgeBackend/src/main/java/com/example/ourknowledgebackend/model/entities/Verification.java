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
    @JoinColumn(name = "usesId")
    private Uses uses;

    public Verification() {
    }

    public Verification(Knowledge knowledge, Uses uses) {
        this.knowledge = knowledge;
        this.uses = uses;
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

    public Uses getUses() {
        return uses;
    }

    public void setUses(Uses uses) {
        this.uses = uses;
    }
}
