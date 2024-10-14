package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.User;

import java.util.Date;
import java.util.Objects;

public class UserResult extends User {

    Long recommendedCount;

    public UserResult(User user, Long recommendedCount) {
        super(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getStartDate());
        this.recommendedCount = recommendedCount;
    }

    public UserResult(Long id, String name, String email, String role, String startDate, Long recommendedCount) {
        super(id, name, email, role, startDate);
        this.recommendedCount = recommendedCount;
    }

    public Long getRecommendedCount() {
        return recommendedCount;
    }

    public void setRecommendedCount(Long recommendedCount) {
        this.recommendedCount = recommendedCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResult that = (UserResult) o;
        return recommendedCount.equals(that.recommendedCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recommendedCount);
    }
}
