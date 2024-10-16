package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Verification;

import java.util.List;

public class UsesTree extends Tree<UsesTechnology>{

    public UsesTree(UsesTechnology parent, List<UsesTree> children) {
        super(parent, children);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<UsesTree> getChildren() {
        return (List<UsesTree>) super.getChildren();
    }
}
