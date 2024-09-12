import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    user: null,
    usersResult: null
};

const user = (state = initialState.user, action) => {

    switch (action.type) {

        case actionTypes.LOGIN_COMPLETED:
            return action.authenticatedUser;

        case actionTypes.LOGOUT:
            return initialState.user;

        default:
            return state;

    }

}

const usersResult = (state = initialState.usersResult, action) => {

    switch (action.type) {

        case actionTypes.FIND_USERS_COMPLETED:
            return action.usersResult;

        case actionTypes.CLEAR_PROJECT_SEARCH:
            return initialState.usersResult;

        default:
            return state;

    }

}

const reducer = combineReducers({
    user,
    usersResult
});

export default reducer;


