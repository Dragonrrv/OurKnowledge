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

export const clearProject = () => ({
    type: actionTypes.CLEAR_PROJECT
});
