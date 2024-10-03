package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Technology;

public class FilterParamTechnology extends Technology {

    private Long filterParamId;

    private Boolean mandatory;

    private Boolean recommended;

    private Boolean unnecessary;

    private Boolean recommendedUnnecessary;

    public FilterParamTechnology(Long id, String name, Long parentId, boolean relevant, Long filterParamId, Boolean mandatory, Boolean recommended, Boolean unnecessary, Boolean recommendedUnnecessary) {
        super(id, name, parentId, relevant);
        this.filterParamId = filterParamId;
        this.mandatory = mandatory;
        this.recommended = recommended;
        this.unnecessary = unnecessary;
        this.recommendedUnnecessary = recommendedUnnecessary;
    }

    public Long getFilterParamId() {
        return filterParamId;
    }

    public void setFilterParamId(Long filterParamId) {
        this.filterParamId = filterParamId;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Boolean getRecommended() {
        return recommended;
    }

    public void setRecommended(Boolean recommended) {
        this.recommended = recommended;
    }

    public Boolean getUnnecessary() {
        return unnecessary;
    }

    public void setUnnecessary(Boolean unnecessary) {
        this.unnecessary = unnecessary;
    }

    public Boolean getRecommendedUnnecessary() {
        return recommendedUnnecessary;
    }

    public void setRecommendedUnnecessary(Boolean recommendedUnnecessary) {
        this.recommendedUnnecessary = recommendedUnnecessary;
    }
}
