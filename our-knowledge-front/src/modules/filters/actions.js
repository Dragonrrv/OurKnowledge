import * as actionTypes from './actionTypes';
import backend from '../../backend';


const findFiltersCompleted = filterListResult => ({
    type: actionTypes.FIND_FILTERS_COMPLETED,
    filterListResult
});

export const findFilters = (userId) => dispatch => {

    dispatch(clearFiltersResult());
    backend.filterService.findFilters(userId,
        result => dispatch(findFiltersCompleted(result)));

}

const clearFiltersResult = () => ({
    type: actionTypes.CLEAR_FILTER_SEARCH
});

const findFilterByIdCompleted = filterDetails => ({
    type: actionTypes.FIND_FILTER_BY_ID_COMPLETED,
    filterDetails
});

export const findFilterById = filterId => dispatch => {
    backend.filterService.findByFilterId(filterId,
        filterDetails => dispatch(findFilterByIdCompleted(filterDetails)));
}

const getDefaultFilterCompleted = filterDetails => ({
    type: actionTypes.FIND_FILTER_BY_ID_COMPLETED,
    filterDetails
});

export const getDefaultFilter = userId => dispatch => {
    backend.filterService.getDefaultFilter(userId,
        filterDetails => dispatch(getDefaultFilterCompleted(filterDetails)));
}

const updateFiltersCompleted = filterListResult => ({
    type: actionTypes.UPDATE_FILTERS_SEARCH,
    filterListResult
});

export const saveFilter = (userId, filterName) => dispatch => {
    backend.filterService.saveFilter(userId, filterName,
        result => dispatch(updateFiltersCompleted(result)));
}

export const deleteFilter = (userId, filterId) => dispatch => {
    backend.filterService.deleteFilter(userId, filterId,
        result => dispatch(updateFiltersCompleted(result)));
}

const updateFilterCompleted = filterDetails => ({
    type: actionTypes.UPDATE_FILTER_COMPLETED,
    filterDetails
});

export const clearFilter = () => ({
    type: actionTypes.CLEAR_FILTER
});

export const addFilterParam = (userId, filterId) => dispatch => {
    backend.filterService.addFilterParam(userId, filterId,
        filterDetails => dispatch(updateFilterCompleted(filterDetails)));
}

export const deleteFilterParam = (filterParamId) => dispatch => {
    backend.filterService.deleteFilterParam(filterParamId,
        filterDetails => dispatch(updateFilterCompleted(filterDetails)));
}

export const updateFilterParam = (userId, filterParamId, filterId, technologyId, mandatory, relevant) => dispatch => {
    backend.filterService.updateFilterParam(userId, filterParamId, filterId, technologyId, mandatory, relevant,
        filterDetails => dispatch(updateFilterCompleted(filterDetails)));
}
