const getModuleState = state => state.projects;

export const getProjectsResult = state =>
    getModuleState(state).projectsResult;

export const getProject = state =>
    getModuleState(state).project;



