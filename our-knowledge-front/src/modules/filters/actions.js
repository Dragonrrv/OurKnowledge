import * as actionTypes from './actionTypes';
import backend from '../../backend';


const findFiltersCompleted = filterList => ({
    type: actionTypes.FIND_FILTERS_COMPLETED,
    filterList
});

export const findFilters = (userId) => dispatch => {

    dispatch(clearFiltersResult());
    backend.filterService.findFilters(userId,
        result => dispatch(findFiltersCompleted(result)));

}

const clearFiltersResult = () => ({
    type: actionTypes.CLEAR_FILTER_SEARCH
});

const findFilterCompleted = filterDetails => ({
    type: actionTypes.FIND_FILTER_COMPLETED,
    filterDetails
});

export const findFilterById = filterId => dispatch => {
    backend.filterService.findByFilterId(filterId,
        filterDetails => dispatch(findFilterCompleted(filterDetails)));
}


export const getDefaultFilter = userId => dispatch => {
    backend.filterService.getDefaultFilter(userId,
        filterDetails => dispatch(findFilterCompleted(filterDetails)));
}

const updateFiltersCompleted = filterList => ({
    type: actionTypes.UPDATE_FILTERS_COMPLETED,
    filterList
});

export const saveFilter = (userId, filterName) => dispatch => {
    backend.filterService.saveFilter(userId, filterName,
        result => dispatch(updateFiltersCompleted(result)));
}

export const clearFilter = (userId) => dispatch => {
    backend.filterService.clearFilter(userId,
        result => dispatch(updateFilterCompleted(result)));
}

export const deleteFilter = (userId, filterId) => dispatch => {
    backend.filterService.deleteFilter(userId, filterId,
        result => dispatch(updateFiltersCompleted(result)));
}

const updateFilterCompleted = filterDetails => ({
    type: actionTypes.UPDATE_FILTER_COMPLETED,
    filterDetails
});

export const updateFilterParam = (userId, filterParamId, filterId, technologyId, mandatory, relevant) => dispatch => {
    backend.filterService.updateFilterParam(userId, filterParamId, filterId, technologyId, mandatory, relevant,
        filterDetails => dispatch(updateFilterCompleted(filterDetails)));
}
