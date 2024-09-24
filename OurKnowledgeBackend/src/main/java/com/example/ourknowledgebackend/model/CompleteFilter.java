package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Filter;

import java.util.List;

public class CompleteFilter {

    private Filter filter;

    private List<FilterParamTree> filterParamTreeList;

    public CompleteFilter() {
    }

    public CompleteFilter(Filter filter, List<FilterParamTree> filterParamTreeList) {
        this.filter = filter;
        this.filterParamTreeList = filterParamTreeList;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public List<FilterParamTree> getFilterParamTechnologyTreeList() {
        return filterParamTreeList;
    }

    public void setFilterParamTechnologyTreeList(List<FilterParamTree> filterParamList) {
        this.filterParamTreeList = filterParamList;
    }
}
