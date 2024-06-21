import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    technologies: null
};

const technologies = (state = initialState.technologies, action) => {

    switch (action.type) {

        case actionTypes.LIST_COMPLETED:
            return action.technologiesTree.technologies;

        default:
            return state;

    }

}

const reducer = combineReducers({
    technologies
});

export default reducer;


