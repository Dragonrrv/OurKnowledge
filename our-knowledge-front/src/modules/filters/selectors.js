const getModuleState = state => state.filters;

export const getFilterList = state =>
    getModuleState(state).filterList;

export const getFilterDetails = state =>
    getModuleState(state).filterDetails;

export const getFilterDetailsId = state =>
    getModuleState(state).filterDetails.filter.id;




