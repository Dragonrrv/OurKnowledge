package com.example.ourknowledgebackend.model.entities;

import org.springframework.data.domain.Slice;

public interface CustomizedUserDao {

    Slice<User> find(int page, String text, int size);
}
