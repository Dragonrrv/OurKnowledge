const getModuleState = state => state.technologies;

export const getTechnologies = state =>
    getModuleState(state).technologies;

export const isEmpty = state =>
    getTechnologies(state) == null

export const getParent = state =>
    isEmpty(state) ? null : getTechnologies(state).parent;

export const getChildren = state =>
    isEmpty(state) ? null : getTechnologies(state).children;



