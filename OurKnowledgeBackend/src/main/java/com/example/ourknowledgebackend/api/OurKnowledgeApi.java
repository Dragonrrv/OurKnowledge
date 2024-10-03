package com.example.ourknowledgebackend.api;

import com.example.ourKnowledge.api.FilterApi;
import com.example.ourKnowledge.api.ProjectApi;
import com.example.ourKnowledge.api.TechnologyApi;
import com.example.ourKnowledge.api.UserApi;
import com.example.ourKnowledge.api.model.*;
import com.example.ourknowledgebackend.exceptions.*;
import com.example.ourknowledgebackend.model.entities.Participation;
import com.example.ourknowledgebackend.model.entities.Project;
import com.example.ourknowledgebackend.model.entities.Uses;
import com.example.ourknowledgebackend.model.entities.Verification;
import com.example.ourknowledgebackend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.directory.InvalidAttributesException;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OurKnowledgeApi implements TechnologyApi, UserApi, ProjectApi, FilterApi {

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
    public ResponseEntity listUsers(ListUsersRequestDTO listUsersRequestDTO) {
        return ResponseEntity.status(200).body(userService.listUsers(listUsersRequestDTO.getPage(),
                listUsersRequestDTO.getKeywords() != null ? listUsersRequestDTO.getKeywords().trim() : null,
                listUsersRequestDTO.getSize()));
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
    public ResponseEntity deleteProject(DeleteProjectRequestDTO deleteProjectRequestDTO) {
        try {
            projectService.deleteProject(longId(deleteProjectRequestDTO.getProjectId()));
            return ResponseEntity.status(200).body(null);
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity addUses(AddUsesRequestDTO addUseRequestDTO) {
        try {
            projectService.addUses(longId(addUseRequestDTO.getProjectId()), longId(addUseRequestDTO.getTechnologyId()));
            return ResponseEntity.status(200).body(projectService.projectDetails(longId(addUseRequestDTO.getProjectId())));
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
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
            Participation participation = projectService.participate(longId(participateRequestDTO.getUserId()),
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
            Participation participation = projectService.updateParticipate(longId(updateParticipateRequestDTO.getParticipationId()),
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
            return ResponseEntity.status(200).body(projectService.listVerification(longId(listVerificationRequestDTO.getUserId()),
                    longId(listVerificationRequestDTO.getProjectId())));
        } catch (InvalidAttributesException e) {
            throw new RuntimeException(e);
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity addVerification(AddVerificationRequestDTO addVerificationRequestDTO){
        try {
            Verification verification = projectService.addVerification(longId(addVerificationRequestDTO.getUserId()),
                    longId(addVerificationRequestDTO.getUsesId()));
            return ResponseEntity.status(200).body(projectService.projectDetails(verification.getUses().getProject().getId()));
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity deleteVerification(DeleteVerificationRequestDTO deleteVerificationRequestDTO){
        try {
            Verification verification = projectService.deleteVerification(
                    longId(deleteVerificationRequestDTO.getVerificationId()),
                    deleteVerificationRequestDTO.getDeleteKnowledge());
            return ResponseEntity.status(200).body(projectService.projectDetails(verification.getUses().getProject().getId()));
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity listFilters(ListFiltersRequestDTO listFiltersRequestDTO) {
        try {
            return ResponseEntity.status(200).body(filterService.listFilter(longId(listFiltersRequestDTO.getUserId())));
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity getFilter(GetFilterRequestDTO getFilterRequestDTO) {
        try {
            return ResponseEntity.status(200).body(filterService.getFilter(longId(getFilterRequestDTO.getFilterId())));
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity getDefaultFilter(GetDefaultFilterRequestDTO getDefaultFilterRequestDTO) {
        try {
            return ResponseEntity.status(200).body(filterService.getDefaultFilter(longId(getDefaultFilterRequestDTO.getUserId())));
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity saveFilter(SaveFilterRequestDTO saveFilterRequestDTO) {
        try {
            filterService.saveFilter(longId(saveFilterRequestDTO.getUserId()), saveFilterRequestDTO.getFilterName());
            return ResponseEntity.status(200).body(filterService.listFilter(longId(saveFilterRequestDTO.getUserId())));
        } catch (InstanceNotFoundException | DuplicateInstanceException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity clearFilter(ClearFilterRequestDTO clearFilterRequestDTO) {
        try {
            filterService.clearFilter(longId(clearFilterRequestDTO.getUserId()));
            return ResponseEntity.status(200).body(filterService.getDefaultFilter(longId(clearFilterRequestDTO.getUserId())));
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity deleteFilter(DeleteFilterRequestDTO deleteFilterRequestDTO) {
        try {
            filterService.deleteFilter(longId(deleteFilterRequestDTO.getUserId()), longId(deleteFilterRequestDTO.getFilterId()));
            return ResponseEntity.status(200).body(filterService.listFilter(longId(deleteFilterRequestDTO.getUserId())));
        } catch (InstanceNotFoundException | PermissionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity updateFilterParam(UpdateFilterParamRequestDTO updateFilterParamRequestDTO) {
        try {
            long filterId = filterService.updateFilterParam(longId(updateFilterParamRequestDTO.getUserId()),
                    longId(updateFilterParamRequestDTO.getFilterParamId()),
                    longId(updateFilterParamRequestDTO.getFilterId()),
                    longId(updateFilterParamRequestDTO.getTechnologyId()),
                    updateFilterParamRequestDTO.getMandatory(),
                    updateFilterParamRequestDTO.getRecommended());
            return ResponseEntity.status(200).body(filterService.getFilter(filterId));
        } catch (InstanceNotFoundException | PermissionException | InvalidAttributesException |
                 DuplicateInstanceException e) {
            throw new RuntimeException(e);
        }
    }

    private Long longId(Integer id){
        return id == null ? null : id.longValue();
    }


}
