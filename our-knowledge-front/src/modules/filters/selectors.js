const getModuleState = state => state.filters;

export const getFilterList = state =>
    getModuleState(state).filterList;

export const getFilterDetails = state =>
    getModuleState(state).filterDetails;

export const getFilterId = state =>
    getModuleState(state).filterDetails?.filter.id || null;

export const getFilterName = state =>
    getModuleState(state).filterDetails?.filter.name || null;




