import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    projectsResult: null,
    project: null,
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

const project = (state = initialState.project, action) => {

    switch (action.type) {

        case actionTypes.FIND_PROJECT_BY_ID_COMPLETED:
            return action.project;

        case actionTypes.CLEAR_PROJECT:
            return initialState.project;

        default:
            return state;

    }

}

const reducer = combineReducers({
    projectsResult,
    project
});

export default reducer;


