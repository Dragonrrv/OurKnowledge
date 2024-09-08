const getModuleState = state => state.projects;

export const getProjectsResult = state =>
    getModuleState(state).projectsResult;

export const getProjectDetails = state =>
    getModuleState(state).projectDetails;

export const getProjectId = state =>
    getModuleState(state).projectDetails.project.id



