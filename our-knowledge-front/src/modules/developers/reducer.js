import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    profile: null,
};

const profile = (state = initialState.profile, action) => {

    switch (action.type) {

        case actionTypes.PROFILE_UPDATED:
            return action.profile;

        case actionTypes.FIND_PROFILE_COMPLETED:
            return action.profile;

        default:
            return state;

    }

}

const reducer = combineReducers({
    profile
});

export default reducer;


