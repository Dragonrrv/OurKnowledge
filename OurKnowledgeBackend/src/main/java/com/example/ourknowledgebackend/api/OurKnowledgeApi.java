package com.example.ourknowledgebackend.api;

import com.example.ourKnowledge.api.FilterApi;
import com.example.ourKnowledge.api.ProjectApi;
import com.example.ourKnowledge.api.TechnologyApi;
import com.example.ourKnowledge.api.UserApi;
import com.example.ourKnowledge.api.model.*;
import com.example.ourknowledgebackend.exceptions.*;
import com.example.ourknowledgebackend.model.UserResult;
import com.example.ourknowledgebackend.model.entities.Participation;
import com.example.ourknowledgebackend.model.entities.Project;
import com.example.ourknowledgebackend.model.entities.Uses;
import com.example.ourknowledgebackend.model.entities.Verification;
import com.example.ourknowledgebackend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.directory.InvalidAttributesException;
import java.io.File;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OurKnowledgeApi implements TechnologyApi, UserApi, ProjectApi, FilterApi {

    private final PermissionChecker permissionChecker;

    private final TechnologyService technologyService;

    private final ProjectService projectService;

    private final UserService userService;

    private final KnowledgeService knowledgeService;

    private final FilterService filterService;

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
            return ResponseEntity.status(200).body(technologyService.addTechnology( permissionChecker.getUserIdByAuthentication(),
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
            return ResponseEntity.status(200).body(technologyService.deleteTechnology(
                    deleteTechnologyRequestDTO.getTechnologyId().longValue(), deleteTechnologyRequestDTO.getDeleteChildren()));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        } catch (PermissionException e) {
            return ResponseEntity.status(403).body(e);
        } catch (HaveChildrenException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer') or hasRole('client_admin')")
    public ResponseEntity listUsers(ListUsersRequestDTO listUsersRequestDTO) {
        try {
            ResponseEntity<Block<UserResult>> a = ResponseEntity.status(200).body(userService.listUsers(listUsersRequestDTO.getPage(),
                    listUsersRequestDTO.getSize(),
                    listUsersRequestDTO.getKeywords() != null ? listUsersRequestDTO.getKeywords().trim() : null,
                    longId(listUsersRequestDTO.getFilterId())));
            return a;
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }

    }

    @Override
    @PreAuthorize("hasRole('client_developer') or hasRole('client_admin')")
    public ResponseEntity showProfile(ProfileRequestDTO profileRequestDTO) {
        try {
            return ResponseEntity.status(200).body(
                    userService.showProfile(longId(profileRequestDTO.getUserId())));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer') or hasRole('client_admin')")
    public ResponseEntity updateUser(UpdateUserRequestDTO updateUserRequestDTO) {
        try {
            userService.updateUser(permissionChecker.getUserIdByAuthentication(),
                    updateUserRequestDTO.getStartDate());
            return ResponseEntity.status(200).body(
                    userService.showProfile(permissionChecker.getUserIdByAuthentication()));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity addKnowledge(AddKnowledgeRequestDTO addKnowledgeRequestDTO) {
        try {
            knowledgeService.addKnowledge(permissionChecker.getUserIdByAuthentication(),
                    longId(addKnowledgeRequestDTO.getTechnologyId()), addKnowledgeRequestDTO.getTechnologyName(),
                    longId(addKnowledgeRequestDTO.getParentTechnologyId()));
            return ResponseEntity.status(200).body(
                    userService.showProfile(permissionChecker.getUserIdByAuthentication()));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        } catch (DuplicateInstanceException e) {
            return ResponseEntity.status(409).body(e);
        } catch (InvalidAttributesException e) {
            return ResponseEntity.status(400).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity deleteKnowledge(DeleteKnowledgeRequestDTO deleteKnowledgeRequestDTO) {
        try {
            knowledgeService.deleteKnowledge(permissionChecker.getUserIdByAuthentication(),
                    deleteKnowledgeRequestDTO.getKnowledgeId().longValue());
            return ResponseEntity.status(200).body(
                    userService.showProfile(permissionChecker.getUserIdByAuthentication()));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        } catch (PermissionException e) {
            return ResponseEntity.status(403).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity updateKnowledge(UpdateKnowledgeRequestDTO updateKnowledgeRequestDTO) {
        try {
            knowledgeService.updateKnowledge(permissionChecker.getUserIdByAuthentication(),
                    updateKnowledgeRequestDTO.getKnowledgeId().longValue(), updateKnowledgeRequestDTO.getMainSkill(),
                    updateKnowledgeRequestDTO.getLikeIt());
            return ResponseEntity.status(200).body(
                    userService.showProfile(permissionChecker.getUserIdByAuthentication()));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        } catch (PermissionException e) {
            return ResponseEntity.status(403).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer') or hasRole('client_admin')")
    public ResponseEntity listProjects(ListProjectsRequestDTO listProjectsRequestDTO){
        try {
            return ResponseEntity.status(200).body(projectService.listProjects(listProjectsRequestDTO.getPage(),
                    listProjectsRequestDTO.getSize(),
                    listProjectsRequestDTO.getKeywords() != null ? listProjectsRequestDTO.getKeywords().trim() : null,
                    longId(listProjectsRequestDTO.getFilterId())));
        } catch (InstanceNotFoundException e){
            return ResponseEntity.status(404).body(e);
        }
    }


    @Override
    @PreAuthorize("hasRole('client_developer') or hasRole('client_admin')")
    public ResponseEntity projectDetails(ProjectDetailsRequestDTO projectDetailsRequestDTO){
        try {
            return ResponseEntity.status(200).body(projectService.projectDetails(longId(projectDetailsRequestDTO.getId())));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity addProject(AddProjectRequestDTO addProjectRequestDTO) {
        try {
            Project project = projectService.addProject( addProjectRequestDTO.getName(),
                    addProjectRequestDTO.getDescription(), addProjectRequestDTO.getStatus(),
                    addProjectRequestDTO.getStartDate(), addProjectRequestDTO.getSize(),
                    addProjectRequestDTO.getTechnologyIdList().stream().map(Integer::longValue).collect(Collectors.toList()));
        return ResponseEntity.status(200).body(projectService.projectDetails(project.getId()));
        } catch (DuplicateInstanceException e) {
            return ResponseEntity.status(409).body(e);
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
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
                    updateProjectRequestDTO.getTechnologyIdList().stream().map(Integer::longValue).collect(Collectors.toList()));
        return ResponseEntity.status(200).body(projectService.projectDetails(longId(updateProjectRequestDTO.getId())));
        } catch (DuplicateInstanceException e) {
            return ResponseEntity.status(409).body(e);
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity updateProjectWithFile(UpdateWithFileRequestDTO updateWithFileRequestDTO) {
        try {
            projectService.updateProjectWithFile( longId(updateWithFileRequestDTO.getId()),
                    updateWithFileRequestDTO.getExtension(), updateWithFileRequestDTO.getFileContent());
        return ResponseEntity.status(200).body(projectService.projectDetails(longId(updateWithFileRequestDTO.getId())));
        } catch (DuplicateInstanceException e) {
            return ResponseEntity.status(409).body(e);
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity deleteProject(DeleteProjectRequestDTO deleteProjectRequestDTO) {
        try {
            projectService.deleteProject(longId(deleteProjectRequestDTO.getProjectId()));
            return ResponseEntity.status(200).body(null);
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity addUses(AddUsesRequestDTO addUseRequestDTO) {
        try {
            projectService.addUses(longId(addUseRequestDTO.getProjectId()), longId(addUseRequestDTO.getTechnologyId()));
            return ResponseEntity.status(200).body(projectService.projectDetails(longId(addUseRequestDTO.getProjectId())));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        } catch (DuplicateInstanceException e) {
            return ResponseEntity.status(409).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity deleteUses(DeleteUsesRequestDTO deleteUsesRequestDTO) {
        try {
            Uses uses = projectService.deleteUses(longId(deleteUsesRequestDTO.getUsesId()));
            return ResponseEntity.status(200).body(projectService.projectDetails(uses.getProject().getId()));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity participate(ParticipateRequestDTO participateRequestDTO){
        try {
            Participation participation = projectService.participate(permissionChecker.getUserIdByAuthentication(),
                    longId(participateRequestDTO.getProjectId()),
                    participateRequestDTO.getStartDate(), participateRequestDTO.getEndDate());
            return ResponseEntity.status(200).body(projectService.projectDetails(participation.getProject().getId()));
        } catch (DuplicateInstanceException e) {
            return ResponseEntity.status(409).body(e);
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity updateParticipate(UpdateParticipateRequestDTO updateParticipateRequestDTO){
        try {
            Participation participation = projectService.updateParticipate(permissionChecker.getUserIdByAuthentication(),
                    updateParticipateRequestDTO.getStartDate(), updateParticipateRequestDTO.getEndDate());
            return ResponseEntity.status(200).body(projectService.projectDetails(participation.getProject().getId()));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity listVerification(ListVerificationRequestDTO listVerificationRequestDTO){
        try {
            return ResponseEntity.status(200).body(projectService.listVerification(permissionChecker.getUserIdByAuthentication(),
                    longId(listVerificationRequestDTO.getProjectId())));
        } catch (InvalidAttributesException e) {
            return ResponseEntity.status(400).body(e);
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity addVerification(AddVerificationRequestDTO addVerificationRequestDTO){
        try {
            Verification verification = projectService.addVerification(permissionChecker.getUserIdByAuthentication(),
                    longId(addVerificationRequestDTO.getUsesId()));
            return ResponseEntity.status(200).body(projectService.projectDetails(verification.getUses().getProject().getId()));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity deleteVerification(DeleteVerificationRequestDTO deleteVerificationRequestDTO){
        try {
            Verification verification = projectService.deleteVerification(
                    permissionChecker.getUserIdByAuthentication(),
                    deleteVerificationRequestDTO.getDeleteKnowledge());
            return ResponseEntity.status(200).body(projectService.projectDetails(verification.getUses().getProject().getId()));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity listFilters() {
        try {
            return ResponseEntity.status(200).body(filterService.listFilter(permissionChecker.getUserIdByAuthentication()));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity getFilter(GetFilterRequestDTO getFilterRequestDTO) {
        try {
            return ResponseEntity.status(200).body(filterService.getFilter(permissionChecker.getUserIdByAuthentication(),
                    longId(getFilterRequestDTO.getFilterId())));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        } catch (PermissionException e) {
            return ResponseEntity.status(403).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity getDefaultFilter() {
        try {
            return ResponseEntity.status(200).body(filterService.getDefaultFilter(permissionChecker.getUserIdByAuthentication()));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity saveFilter(SaveFilterRequestDTO saveFilterRequestDTO) {
        try {
            filterService.saveFilter(permissionChecker.getUserIdByAuthentication(), saveFilterRequestDTO.getFilterName());
            return ResponseEntity.status(200).body(filterService.listFilter(permissionChecker.getUserIdByAuthentication()));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        } catch (DuplicateInstanceException e) {
            return ResponseEntity.status(409).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity clearFilter() {
        try {
            filterService.clearFilter(permissionChecker.getUserIdByAuthentication());
            return ResponseEntity.status(200).body(filterService.getDefaultFilter(permissionChecker.getUserIdByAuthentication()));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity deleteFilter(DeleteFilterRequestDTO deleteFilterRequestDTO) {
        try {
            filterService.deleteFilter(permissionChecker.getUserIdByAuthentication(), longId(deleteFilterRequestDTO.getFilterId()));
            return ResponseEntity.status(200).body(filterService.listFilter(permissionChecker.getUserIdByAuthentication()));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        } catch (PermissionException e) {
            return ResponseEntity.status(403).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity createFilterByProject(CreateByProjectRequestDTO createByProjectRequestDTO) {
        try {
            filterService.createByProject(permissionChecker.getUserIdByAuthentication(), longId(createByProjectRequestDTO.getProjectId()));
            return ResponseEntity.status(200).body(filterService.getDefaultFilter(permissionChecker.getUserIdByAuthentication()));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity createFilterByUser(CreateByUserRequestDTO createByUserRequestDTO) {
        try {
            filterService.createByUser(permissionChecker.getUserIdByAuthentication(), longId(createByUserRequestDTO.getUserId()));
            return ResponseEntity.status(200).body(filterService.getDefaultFilter(permissionChecker.getUserIdByAuthentication()));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity updateFilterParam(UpdateFilterParamRequestDTO updateFilterParamRequestDTO) {
        try {
            long filterId = filterService.updateFilterParam(permissionChecker.getUserIdByAuthentication(),
                    longId(updateFilterParamRequestDTO.getFilterParamId()),
                    longId(updateFilterParamRequestDTO.getFilterId()),
                    longId(updateFilterParamRequestDTO.getTechnologyId()),
                    updateFilterParamRequestDTO.getMandatory(),
                    updateFilterParamRequestDTO.getRecommended());
            return ResponseEntity.status(200).body(filterService.getFilter(permissionChecker.getUserIdByAuthentication(),
                    filterId));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        } catch (DuplicateInstanceException e) {
            return ResponseEntity.status(409).body(e);
        } catch (PermissionException e) {
            return ResponseEntity.status(403).body(e);
        } catch (InvalidAttributesException e) {
            return ResponseEntity.status(400).body(e);
        }
    }

    private Long longId(Integer id){
        return id == null ? null : id.longValue();
    }


}
