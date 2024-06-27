package com.example.ourknowledgebackend.api;

import com.example.ourKnowledge.api.TechnologyApi;
import com.example.ourKnowledge.api.UserApi;
import com.example.ourKnowledge.api.model.*;
import com.example.ourknowledgebackend.api.mappers.ApiTechnologyMapper;
import com.example.ourknowledgebackend.exceptions.*;
import com.example.ourknowledgebackend.model.entities.Technology;
import com.example.ourknowledgebackend.service.KnowledgeService;
import com.example.ourknowledgebackend.service.impl.TechnologyServiceImpl;
import com.example.ourknowledgebackend.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OurKnowledgeApi implements TechnologyApi, UserApi {

    private final TechnologyServiceImpl technologyServiceImpl;

    private final UserServiceImpl userServiceImpl;

    private final KnowledgeService knowledgeService;

    @Override
    public ResponseEntity login(LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.status(200).body(userServiceImpl.login(loginRequestDTO.getUserName(), loginRequestDTO.getPassword()));
    }

    @Override
    public ResponseEntity listRelevantTechnologies() {
        return ResponseEntity.status(200).body(technologyServiceImpl.listRelevantTechnologies());
    }

    @Override
    public ResponseEntity addTechnology(AddTechnologyRequestDTO addTechnologyRequestDTO) {
        Long parentId = addTechnologyRequestDTO.getParentId() != null ? addTechnologyRequestDTO.getParentId().longValue() : null;
        try {
            return ResponseEntity.status(200).body(technologyServiceImpl.addTechnology(addTechnologyRequestDTO.getName(), parentId, addTechnologyRequestDTO.getUserId().longValue()));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        } catch (DuplicateInstanceException e) {
            return ResponseEntity.status(409).body(e);
        }
    }

    @Override
    public ResponseEntity deleteTechnology(DeleteTechnologyRequestDTO deleteTechnologyRequestDTO) {
        try {
            return ResponseEntity.status(200).body(technologyServiceImpl.deleteTechnology(deleteTechnologyRequestDTO.getUserId().longValue(),
                    deleteTechnologyRequestDTO.getTechnologyId().longValue(), deleteTechnologyRequestDTO.getDeleteChildren()));
        } catch (InstanceNotFoundException | HaveChildrenException | PermissionException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity showProfile(ProfileRequestDTO profileRequestDTO) {
        return ResponseEntity.status(200).body(
                userServiceImpl.showProfile(profileRequestDTO.getProfileId().longValue(),
                        profileRequestDTO.getUserId().longValue()));
    }

    @Override
    public ResponseEntity addKnowledge(AddKnowledgeRequestDTO addKnowledgeRequestDTO) {
        try {
            knowledgeService.addKnowledge(addKnowledgeRequestDTO.getUserId().longValue(),
                    addKnowledgeRequestDTO.getTechnologyId().longValue());
        } catch (InstanceNotFoundException | DuplicateInstanceException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(200).body("Knowledge added");
    }

    @Override
    public ResponseEntity deleteKnowledge(DeleteKnowledgeRequestDTO deleteKnowledgeRequestDTO) {
        try {
            knowledgeService.deleteKnowledge(deleteKnowledgeRequestDTO.getUserId().longValue(),
                    deleteKnowledgeRequestDTO.getKnowledgeId().longValue());
        } catch (InstanceNotFoundException | PermissionException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(200).body("Knowledge deleted");
    }

    @Override
    public ResponseEntity updateKnowledge(UpdateKnowledgeRequestDTO updateKnowledgeRequestDTO) {
        try {
            knowledgeService.updateKnowledge(updateKnowledgeRequestDTO.getUserId().longValue(),
                    updateKnowledgeRequestDTO.getKnowledgeId().longValue(), updateKnowledgeRequestDTO.getMainSkill(),
                    updateKnowledgeRequestDTO.getLikeIt());
        } catch (InstanceNotFoundException | PermissionException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(200).body("Knowledge updated");
    }

}
