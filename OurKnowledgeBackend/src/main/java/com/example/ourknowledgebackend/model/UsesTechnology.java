package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Knowledge;
import com.example.ourknowledgebackend.model.entities.Technology;

public class UsesTechnology extends Technology {

    private Long usesId;

    public UsesTechnology() {
        super();
    }

    public UsesTechnology(Long usesId) {
        this.usesId = usesId;
    }

    public UsesTechnology(String name, Long parentId, boolean relevant, Long usesId) {
        super(name, parentId, relevant);
        this.usesId = usesId;
    }

    public UsesTechnology(Long id, String name, Long parentId, boolean relevant, Long usesId) {
        super(id, name, parentId, relevant);
        this.usesId = usesId;
    }

    public Long getUsesId() {
        return usesId;
    }

    public void setUsesId(Long usesId) {
        this.usesId = usesId;
    }
}
