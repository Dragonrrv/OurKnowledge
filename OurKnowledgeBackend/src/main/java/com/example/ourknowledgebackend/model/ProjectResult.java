package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Project;

import java.util.Objects;

public class ProjectResult extends Project {

    Long recommendedCount;

    public ProjectResult() {
        super();
    }

    public ProjectResult(Project project, Long recommendedCount) {
        super(project.getId(), project.getName(), project.getDescription(), project.getStatus(), project.getStartDate(), project.getSize());
        this.recommendedCount = recommendedCount;
    }

    public ProjectResult(Long id, String name, String description, String status, String startDate, int size, Long recommendedCount) {
        super(id, name, description, status, startDate, size);
        this.recommendedCount = recommendedCount;
    }

    public ProjectResult(String name, String description, String status, String startDate, int size, Long recommendedCount) {
        super(name, description, status, startDate, size);
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
        ProjectResult that = (ProjectResult) o;
        return recommendedCount.equals(that.recommendedCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recommendedCount);
    }
}
