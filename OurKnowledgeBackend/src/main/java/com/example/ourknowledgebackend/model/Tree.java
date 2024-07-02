package com.example.ourknowledgebackend.model;

import java.util.List;

public abstract class Tree<T>{
    private T parent;
    private List<? extends Tree<T>> children;

    public Tree(T parent, List<? extends Tree<T>> children) {
        this.parent = parent;
        this.children = children;
    }

    public T getParent() {
        return parent;
    }

    public void setParent(T parent) {
        this.parent = parent;
    }

    public List<? extends Tree<T>> getChildren() {
        return children;
    }

    public void setChildren(List<? extends Tree<T>> children) {
        this.children = children;
    }
}
