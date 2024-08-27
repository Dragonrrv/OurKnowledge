import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    technologies: null,
};

const technologies = (state = initialState.technologies, action) => {

    switch (action.type) {

        case actionTypes.TECHNOLOGIES_UPDATED:
            return action.technologies;

        case actionTypes.FIND_TECHNOLOGIES_COMPLETED:
            return action.technologies;

        default:
            return state;

    }

}

const reducer = combineReducers({
    technologies
});

export default reducer;


