import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    user: {
        id: 1,
        name: "Juan",
        email: "Juan@ejemplo.com",
        role: "Admin"
    }
};

const user = (state = initialState.user, action) => {

    switch (action.type) {

        case actionTypes.LOGIN_COMPLETED:
            return action.authenticatedUser.user;

        case actionTypes.LOGOUT:
            return initialState.user;

        default:
            return state;

    }

}

const reducer = combineReducers({
    user
});

export default reducer;


