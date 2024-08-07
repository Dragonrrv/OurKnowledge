package com.example.ourknowledgebackend.api;

import com.example.ourKnowledge.api.ProjectApi;
import com.example.ourKnowledge.api.TechnologyApi;
import com.example.ourKnowledge.api.UserApi;
import com.example.ourKnowledge.api.model.*;
import com.example.ourknowledgebackend.exceptions.*;
import com.example.ourknowledgebackend.service.KnowledgeService;
import com.example.ourknowledgebackend.service.impl.ProjectServiceImpl;
import com.example.ourknowledgebackend.service.impl.TechnologyServiceImpl;
import com.example.ourknowledgebackend.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.directory.InvalidAttributesException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OurKnowledgeApi implements TechnologyApi, UserApi, ProjectApi {

    private final TechnologyServiceImpl technologyServiceImpl;

    private final ProjectServiceImpl projectServiceImpl;

    private final UserServiceImpl userServiceImpl;

    private final KnowledgeService knowledgeService;

    @Override
    @PreAuthorize("hasRole('client_developer') or hasRole('client_admin')")
    public ResponseEntity login(LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.status(200).body(userServiceImpl.login(loginRequestDTO.getUserName(),
                loginRequestDTO.getEmail(), loginRequestDTO.getRole()));
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity listRelevantTechnologies() {
        return ResponseEntity.status(200).body(technologyServiceImpl.listRelevantTechnologies());
    }



    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity addTechnology(AddTechnologyRequestDTO addTechnologyRequestDTO) {
        Long parentId = addTechnologyRequestDTO.getParentId() != null ? addTechnologyRequestDTO.getParentId().longValue() : null;
        try {
            return ResponseEntity.status(200).body(technologyServiceImpl.addTechnology( addTechnologyRequestDTO.getUserId().longValue(),
                    addTechnologyRequestDTO.getName(), parentId, true));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        } catch (DuplicateInstanceException e) {
            return ResponseEntity.status(409).body(e);
        } catch (PermissionException e) {
            return ResponseEntity.status(403).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
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
    @PreAuthorize("hasRole('client_developer') or hasRole('client_admin')")
    public ResponseEntity showProfile(ProfileRequestDTO profileRequestDTO) {
        return ResponseEntity.status(200).body(
                userServiceImpl.showProfile(profileRequestDTO.getProfileId().longValue(),
                        profileRequestDTO.getUserId().longValue()));
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity addKnowledge(AddKnowledgeRequestDTO addKnowledgeRequestDTO) {
        Long technologyId = addKnowledgeRequestDTO.getTechnologyId()==null ? null : addKnowledgeRequestDTO.getTechnologyId().longValue();
        Long parentTechnologyId = addKnowledgeRequestDTO.getParentTechnologyId()==null ? null : addKnowledgeRequestDTO.getParentTechnologyId().longValue();
        try {
            knowledgeService.addKnowledge(addKnowledgeRequestDTO.getUserId().longValue(),
                    technologyId, addKnowledgeRequestDTO.getTechnologyName(),
                    parentTechnologyId);
            return ResponseEntity.status(200).body(
                    userServiceImpl.showProfile(addKnowledgeRequestDTO.getUserId().longValue(),
                            addKnowledgeRequestDTO.getUserId().longValue()));
        } catch (InstanceNotFoundException | DuplicateInstanceException | InvalidAttributesException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity deleteKnowledge(DeleteKnowledgeRequestDTO deleteKnowledgeRequestDTO) {
        try {
            knowledgeService.deleteKnowledge(deleteKnowledgeRequestDTO.getUserId().longValue(),
                    deleteKnowledgeRequestDTO.getKnowledgeId().longValue());
            return ResponseEntity.status(200).body(
                    userServiceImpl.showProfile(deleteKnowledgeRequestDTO.getUserId().longValue(),
                            deleteKnowledgeRequestDTO.getUserId().longValue()));
        } catch (InstanceNotFoundException | PermissionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity updateKnowledge(UpdateKnowledgeRequestDTO updateKnowledgeRequestDTO) {
        try {
            knowledgeService.updateKnowledge(updateKnowledgeRequestDTO.getUserId().longValue(),
                    updateKnowledgeRequestDTO.getKnowledgeId().longValue(), updateKnowledgeRequestDTO.getMainSkill(),
                    updateKnowledgeRequestDTO.getLikeIt());
            return ResponseEntity.status(200).body(
                    userServiceImpl.showProfile(updateKnowledgeRequestDTO.getUserId().longValue(),
                            updateKnowledgeRequestDTO.getUserId().longValue()));
        } catch (InstanceNotFoundException | PermissionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer') or hasRole('client_admin')")
    public ResponseEntity listProjects() {
        return ResponseEntity.status(200).body(projectServiceImpl.listProjects());
    }


    @Override
    @PreAuthorize("hasRole('client_developer') or hasRole('client_admin')")
    public ResponseEntity projectDetails(ProjectDetailsRequestDTO projectDetailsRequestDTO){
        Long projectId = projectDetailsRequestDTO.getProjectId()==null ? null : projectDetailsRequestDTO.getProjectId().longValue();
        try {
            return ResponseEntity.status(200).body(projectServiceImpl.projectDetails(projectId));
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity addProject(AddProjectRequestDTO addProjectRequestDTO) {
        try {
            projectServiceImpl.addProject( addProjectRequestDTO.getName(),
                    addProjectRequestDTO.getDescription(), addProjectRequestDTO.getStatus(),
                    addProjectRequestDTO.getStartDate(), addProjectRequestDTO.getSize(),
                    addProjectRequestDTO.getTechnologies().stream().map(Integer::longValue).collect(Collectors.toList()));
        return ResponseEntity.status(200).body(null);
        } catch (DuplicateInstanceException e) {
            return ResponseEntity.status(409).body(e);
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity deleteProject(DeleteProjectRequestDTO deleteProjectRequestDTO) {
        try {
            projectServiceImpl.deleteProject(
                    deleteProjectRequestDTO.getProjectId().longValue());
            return ResponseEntity.status(200).body(null);
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
