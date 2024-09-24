import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    filterList: null,
    filterDetails: null,
};


const filterList = (state = initialState.filterList, action) => {

    switch (action.type) {

        case actionTypes.FIND_FILTERS_COMPLETED:
            return action.filterList;

        case actionTypes.CLEAR_FILTER_SEARCH:
            return initialState.filterList;

        default:
            return state;

    }

}

const filterDetails = (state = initialState.filterDetails, action) => {

    switch (action.type) {

        case actionTypes.FIND_FILTER_BY_ID_COMPLETED:
            return action.filterDetails;

        case actionTypes.SAVE_FILTER_COMPLETED:
            return action.filterDetails;

        case actionTypes.UPDATE_FILTER_COMPLETED:
            return action.filterDetails;

        case actionTypes.CLEAR_FILTER:
            return initialState.filterDetails;

        default:
            return state;

    }

}

const reducer = combineReducers({
    filterList,
    filterDetails
});

export default reducer;


