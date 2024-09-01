import * as actionTypes from './actionTypes';
import backend from '../../backend';


const findProjectsCompleted = projectsResult => ({
    type: actionTypes.FIND_PROJECTS_COMPLETED,
    projectsResult
});

export const findProjects = (page, keywords) => dispatch => {

    dispatch(clearProjectsResult());
    backend.projectService.findProjects(page, keywords,
        result => dispatch(findProjectsCompleted(result)));

}

const clearProjectsResult = () => ({
    type: actionTypes.CLEAR_PROJECT_SEARCH
});

const findProjectByIdCompleted = project => ({
    type: actionTypes.FIND_PROJECT_BY_ID_COMPLETED,
    project
});

export const findProjectById = id => dispatch => {
    backend.projectService.findByProjectId(id,
        project => dispatch(findProjectByIdCompleted(project)));
}

const addProjectCompleted = project => ({
    type: actionTypes.ADD_PROJECT_COMPLETED,
    project
});

export const addProject = (name, description, startDate, status, size, technologyIdList) => dispatch => {
    backend.projectService.addProject(name, description, startDate, status, size, technologyIdList,
        project => dispatch(addProjectCompleted(project)));
}

const updateProjectCompleted = project => ({
    type: actionTypes.UPDATE_PROJECT_COMPLETED,
    project
});

export const updateProject = (id, name, description, startDate, status, size, updateTechnologies, technologyIdList) => dispatch => {
    backend.projectService.updateProject(id, name, description, startDate, status, size, updateTechnologies, technologyIdList,
        project => dispatch(updateProjectCompleted(project)));
}

export const clearProject = () => ({
    type: actionTypes.CLEAR_PROJECT
});

export const addUses = (projectId, technologyId) => dispatch => {
    backend.projectService.addUses(projectId, technologyId,
        project => dispatch(updateProjectCompleted(project)));
}

export const deleteUses = (usesId) => dispatch => {
    backend.projectService.deleteUses(usesId,
        project => dispatch(updateProjectCompleted(project)));
}
