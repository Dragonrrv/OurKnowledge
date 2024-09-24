package com.example.ourknowledgebackend.model;

import java.util.List;

public class FilterParamTree extends Tree<FilterParamTechnology> {

    public FilterParamTree(FilterParamTechnology parent, List<FilterParamTree> children) {
        super(parent, children);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<FilterParamTree> getChildren() {
        return (List<FilterParamTree>) super.getChildren();
    }

}
