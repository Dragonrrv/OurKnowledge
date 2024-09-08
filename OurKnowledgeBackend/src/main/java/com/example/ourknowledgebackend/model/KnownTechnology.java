package com.example.ourknowledgebackend.model;

import com.example.ourknowledgebackend.model.entities.Knowledge;
import com.example.ourknowledgebackend.model.entities.Technology;

import java.util.List;

public class KnownTechnology extends Technology {

    private Long knowledgeId;
    private Boolean mainSkill;
    private Boolean likeIt;

    private List<SimpleVerification> verificationList;

    public KnownTechnology() {
        super();
    }

    public KnownTechnology(Long id, String name, Long parentId, boolean relevant, Long knowledgeId, Boolean mainSkill,
                           Boolean likeIt, List<SimpleVerification> verificationList) {
        super(id, name, parentId, relevant);
        this.knowledgeId = knowledgeId;
        this.mainSkill = mainSkill;
        this.likeIt = likeIt;
        this.verificationList = verificationList;
    }

    public KnownTechnology(Technology technology, Knowledge knowledge, List<SimpleVerification> verificationList) {
        super(technology.getName(), technology.getParentId(), technology.isRelevant());
        this.knowledgeId = knowledge.getId();
        this.mainSkill = knowledge.isMainSkill();
        this.likeIt = knowledge.isLikeIt();
        this.verificationList = verificationList;
    }

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public Boolean getMainSkill() {
        return mainSkill;
    }

    public void setMainSkill(Boolean mainSkill) {
        this.mainSkill = mainSkill;
    }

    public Boolean getLikeIt() {
        return likeIt;
    }

    public void setLikeIt(Boolean likeIt) {
        this.likeIt = likeIt;
    }

    public List<SimpleVerification> getVerificationList() {
        return verificationList;
    }

    public void setVerificationList(List<SimpleVerification> verificationList) {
        this.verificationList = verificationList;
    }
}
