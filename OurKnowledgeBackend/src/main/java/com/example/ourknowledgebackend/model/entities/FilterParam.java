package com.example.ourknowledgebackend.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Filterparam")
public class FilterParam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "filterId")
    private Filter filter;

    @ManyToOne
    @JoinColumn(name = "technologyId")
    private Technology technology;

    private boolean mandatory;

    private boolean recommended;

    public FilterParam() {
    }

    public FilterParam(Filter filter, Technology technology, boolean mandatory, boolean recommended) {
        this.filter = filter;
        this.technology = technology;
        this.mandatory = mandatory;
        this.recommended = recommended;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Technology getTechnology() {
        return technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }


}
