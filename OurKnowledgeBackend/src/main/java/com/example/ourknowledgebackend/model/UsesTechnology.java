package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Technology;
import com.example.ourknowledgebackend.model.entities.Verification;
import com.example.ourknowledgebackend.model.entities.VerificationDao;

import java.util.List;

public class UsesTechnology extends Technology {

    private Long usesId;

    private List<SimpleVerification> verificationList;

    public UsesTechnology() {
        super();
    }

    public UsesTechnology(Long usesId) {
        this.usesId = usesId;
    }

    public UsesTechnology(String name, Long parentId, boolean relevant, Long usesId, List<SimpleVerification> verificationList) {
        super(name, parentId, relevant);
        this.usesId = usesId;
        this.verificationList = verificationList;
    }

    public UsesTechnology(Long id, String name, Long parentId, boolean relevant, Long usesId, List<SimpleVerification> verificationList) {
        super(id, name, parentId, relevant);
        this.usesId = usesId;
        this.verificationList = verificationList;
    }

    public Long getUsesId() {
        return usesId;
    }

    public void setUsesId(Long usesId) {
        this.usesId = usesId;
    }

    public List<SimpleVerification> getVerificationList() {
        return verificationList;
    }


    public void setVerificationList(List<SimpleVerification> verificationList) {
        this.verificationList = verificationList;
    }
}
