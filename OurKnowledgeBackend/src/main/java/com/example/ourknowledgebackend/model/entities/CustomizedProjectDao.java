package com.example.ourknowledgebackend.model.entities;

import org.springframework.data.domain.Slice;

import java.util.List;

public interface CustomizedProjectDao {

    Slice<Project> find(int page, int size, String keywords, List<Long> mandatoryList,  List<Long> recommendedList);
}
