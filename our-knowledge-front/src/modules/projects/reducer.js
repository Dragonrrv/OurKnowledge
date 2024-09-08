import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    projectsResult: null,
    projectDetails: null,
};


const projectsResult = (state = initialState.projectsResult, action) => {

    switch (action.type) {

        case actionTypes.FIND_PROJECTS_COMPLETED:
            return action.projectsResult;

        case actionTypes.CLEAR_PROJECT_SEARCH:
            return initialState.projectsResult;

        default:
            return state;

    }

}

const projectDetails = (state = initialState.projectDetails, action) => {

    switch (action.type) {

        case actionTypes.FIND_PROJECT_BY_ID_COMPLETED:
            return action.projectDetails;

        case actionTypes.ADD_PROJECT_COMPLETED:
            return action.projectDetails;

        case actionTypes.UPDATE_PROJECT_COMPLETED:
            return action.projectDetails;

        case actionTypes.CLEAR_PROJECT:
            return initialState.projectDetails;

        default:
            return state;

    }

}

const reducer = combineReducers({
    projectsResult,
    projectDetails
});

export default reducer;


