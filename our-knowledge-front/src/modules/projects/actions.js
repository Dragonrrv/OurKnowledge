import * as actionTypes from './actionTypes';
import backend from '../../backend';


const findProjectsCompleted = projectsResult => ({
    type: actionTypes.FIND_PROJECTS_COMPLETED,
    projectsResult
});

export const findProjects = (page, keywords, filterId) => dispatch => {

    dispatch(clearProjectsResult());
    backend.projectService.findProjects(page, keywords, filterId,
        result => dispatch(findProjectsCompleted(result)));

}

const clearProjectsResult = () => ({
    type: actionTypes.CLEAR_PROJECT_SEARCH
});

const findProjectByIdCompleted = projectDetails => ({
    type: actionTypes.FIND_PROJECT_BY_ID_COMPLETED,
    projectDetails
});

export const findProjectById = id => dispatch => {
    backend.projectService.findByProjectId(id,
        projectDetails => dispatch(findProjectByIdCompleted(projectDetails)));
}

const addProjectCompleted = projectDetails => ({
    type: actionTypes.ADD_PROJECT_COMPLETED,
    projectDetails
});

export const addProject = (name, description, startDate, status, size, technologyIdList) => dispatch => {
    backend.projectService.addProject(name, description, startDate, status, size, technologyIdList,
        projectDetails => dispatch(addProjectCompleted(projectDetails)));
}

const updateProjectCompleted = projectDetails => ({
    type: actionTypes.UPDATE_PROJECT_COMPLETED,
    projectDetails
});

export const updateProject = (id, name, description, startDate, status, size, updateTechnologies, technologyIdList) => dispatch => {
    backend.projectService.updateProject(id, name, description, startDate, status, size, updateTechnologies, technologyIdList,
        projectDetails => dispatch(updateProjectCompleted(projectDetails)));
}

export const sendFile = (id, extension, fileContent) => dispatch => {
    backend.projectService.sendProjectFile(id, extension, fileContent,
        projectDetails => dispatch(updateProjectCompleted(projectDetails)));
}

export const clearProject = () => ({
    type: actionTypes.CLEAR_PROJECT
});

export const addUses = (projectId, technologyId) => dispatch => {
    backend.projectService.addUses(projectId, technologyId,
        projectDetails => dispatch(updateProjectCompleted(projectDetails)));
}

export const deleteUses = (usesId) => dispatch => {
    backend.projectService.deleteUses(usesId,
        projectDetails => dispatch(updateProjectCompleted(projectDetails)));
}

export const addParticipation = (projectId, startDate, endDate) => dispatch => {
    backend.projectService.addParticipation(projectId, startDate, endDate,
        projectDetails => dispatch(updateProjectCompleted(projectDetails)));
}

export const updateParticipation = (participationId, startDate, endDate) => dispatch => {
    backend.projectService.updateParticipation(participationId, startDate, endDate,
        projectDetails => dispatch(updateProjectCompleted(projectDetails)));
}

export const addVerification = (usesId) => dispatch => {
    backend.projectService.addVerification(usesId,
        projectDetails => dispatch(updateProjectCompleted(projectDetails)));
}

export const deleteVerification = (verificationId, deleteKnowledge) => dispatch => {
    backend.projectService.deleteVerification(verificationId, deleteKnowledge,
        projectDetails => dispatch(updateProjectCompleted(projectDetails)));
}
