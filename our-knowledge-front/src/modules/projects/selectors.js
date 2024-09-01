const getModuleState = state => state.projects;

export const getProjectsResult = state =>
    getModuleState(state).projectsResult;

export const getProject = state =>
    getModuleState(state).project;

export const getProjectId = state =>
    getModuleState(state).project.project.id



