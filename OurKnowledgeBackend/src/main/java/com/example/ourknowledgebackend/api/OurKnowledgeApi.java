package com.example.ourknowledgebackend.api;

import com.example.ourKnowledge.api.ProjectApi;
import com.example.ourKnowledge.api.TechnologyApi;
import com.example.ourKnowledge.api.UserApi;
import com.example.ourKnowledge.api.model.*;
import com.example.ourknowledgebackend.exceptions.*;
import com.example.ourknowledgebackend.service.KnowledgeService;
import com.example.ourknowledgebackend.service.ProjectService;
import com.example.ourknowledgebackend.service.TechnologyService;
import com.example.ourknowledgebackend.service.UserService;
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

    private final TechnologyService technologyService;

    private final ProjectService projectService;

    private final UserService userService;

    private final KnowledgeService knowledgeService;

    @Override
    @PreAuthorize("hasRole('client_developer') or hasRole('client_admin')")
    public ResponseEntity login(LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.status(200).body(userService.login(loginRequestDTO.getUserName(),
                loginRequestDTO.getEmail(), loginRequestDTO.getRole()));
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity listRelevantTechnologies() {
        return ResponseEntity.status(200).body(technologyService.listRelevantTechnologies());
    }



    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity addTechnology(AddTechnologyRequestDTO addTechnologyRequestDTO) {
        try {
            return ResponseEntity.status(200).body(technologyService.addTechnology( addTechnologyRequestDTO.getUserId().longValue(),
                    addTechnologyRequestDTO.getName(), longId(addTechnologyRequestDTO.getParentId()), true));
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
            return ResponseEntity.status(200).body(technologyService.deleteTechnology(deleteTechnologyRequestDTO.getUserId().longValue(),
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
                userService.showProfile(profileRequestDTO.getProfileId().longValue(),
                        profileRequestDTO.getUserId().longValue()));
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity addKnowledge(AddKnowledgeRequestDTO addKnowledgeRequestDTO) {
        try {
            knowledgeService.addKnowledge(addKnowledgeRequestDTO.getUserId().longValue(),
                    longId(addKnowledgeRequestDTO.getTechnologyId()), addKnowledgeRequestDTO.getTechnologyName(),
                    longId(addKnowledgeRequestDTO.getParentTechnologyId()));
            return ResponseEntity.status(200).body(
                    userService.showProfile(addKnowledgeRequestDTO.getUserId().longValue(),
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
                    userService.showProfile(deleteKnowledgeRequestDTO.getUserId().longValue(),
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
                    userService.showProfile(updateKnowledgeRequestDTO.getUserId().longValue(),
                            updateKnowledgeRequestDTO.getUserId().longValue()));
        } catch (InstanceNotFoundException | PermissionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer') or hasRole('client_admin')")
    public ResponseEntity listProjects(ListProjectsRequestDTO listProjectsRequestDTO) {
        return ResponseEntity.status(200).body(projectService.listProjects(listProjectsRequestDTO.getPage(),
                listProjectsRequestDTO.getKeywords() != null ? listProjectsRequestDTO.getKeywords().trim() : null,
                10));
    }


    @Override
    @PreAuthorize("hasRole('client_developer') or hasRole('client_admin')")
    public ResponseEntity projectDetails(ProjectDetailsRequestDTO projectDetailsRequestDTO){
        try {
            return ResponseEntity.status(200).body(projectService.projectDetails(longId(projectDetailsRequestDTO.getProjectId())));
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity addProject(AddProjectRequestDTO addProjectRequestDTO) {
        try {
            projectService.addProject( addProjectRequestDTO.getName(),
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
    public ResponseEntity updateProject(UpdateProjectRequestDTO updateProjectRequestDTO) {
        try {
            projectService.updateProject( longId(updateProjectRequestDTO.getId()), updateProjectRequestDTO.getName(),
                    updateProjectRequestDTO.getDescription(), updateProjectRequestDTO.getStatus(),
                    updateProjectRequestDTO.getStartDate(), updateProjectRequestDTO.getSize(),
                    updateProjectRequestDTO.getUpdateTechnologies(),
                    updateProjectRequestDTO.getTechnologies().stream().map(Integer::longValue).collect(Collectors.toList()));
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
            projectService.deleteProject(longId(deleteProjectRequestDTO.getProjectId()));
            return ResponseEntity.status(200).body(null);
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity participate(ParticipateRequestDTO participateRequestDTO){
        try {
            projectService.participate(longId(participateRequestDTO.getUserId()),
                    longId(participateRequestDTO.getProjectId()),
                    participateRequestDTO.getStartDate(), participateRequestDTO.getEndDate());
            return ResponseEntity.status(200).body(null);
        } catch (DuplicateInstanceException e) {
            return ResponseEntity.status(409).body(e);
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity updateParticipate(UpdateParticipateRequestDTO updateParticipateRequestDTO){
        try {
            projectService.updateParticipate(longId(updateParticipateRequestDTO.getParticipationId()),
                    updateParticipateRequestDTO.getStartDate(), updateParticipateRequestDTO.getEndDate());
            return ResponseEntity.status(200).body(null);
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity verificate(VerificateRequestDTO verificateRequestDTO){
        List<Long> technologiesId = verificateRequestDTO.getTechnologiesId().stream().map(Integer::longValue).collect(Collectors.toList());
        try {
            projectService.verificate(longId(verificateRequestDTO.getProjectId()),
                    longId(verificateRequestDTO.getProjectId()), technologiesId);
            return ResponseEntity.status(200).body(null);
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Long longId(Integer id){
        return id == null ? null : id.longValue();
    }


}
