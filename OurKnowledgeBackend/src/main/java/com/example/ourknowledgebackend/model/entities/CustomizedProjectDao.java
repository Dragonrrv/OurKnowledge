package com.example.ourknowledgebackend.model.entities;

import com.example.ourknowledgebackend.model.ProjectResult;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CustomizedProjectDao {

    Slice<ProjectResult> find(int page, int size, String keywords, List<Long> mandatoryList, List<Long> recommendedList);
}
