package com.example.ourknowledgebackend.model.entities;

import org.springframework.data.domain.Slice;

public interface CustomizedProjectDao {

    Slice<Project> find(int page, String text, int size);
}
