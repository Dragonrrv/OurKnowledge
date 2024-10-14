package com.example.ourknowledgebackend.model.entities;

import com.example.ourknowledgebackend.model.UserResult;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CustomizedUserDao {

    Slice<UserResult> find(int page, int size, String tokens, List<Long> mandatoryList, List<Long> recommendedList);
}
