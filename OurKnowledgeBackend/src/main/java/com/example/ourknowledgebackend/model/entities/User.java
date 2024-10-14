package com.example.ourknowledgebackend.model.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String role;
    private String startDate;

    public User() {

    }

    public User(Long id, String name, String email, String role, String startDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.startDate = startDate;
    }

    public User(String name, String email, String role, String startDate) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.startDate = startDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
