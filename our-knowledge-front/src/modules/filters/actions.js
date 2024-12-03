import * as actionTypes from './actionTypes';
import backend from '../../backend';


const findFiltersCompleted = filterList => ({
    type: actionTypes.FIND_FILTERS_COMPLETED,
    filterList
});

export const findFilters = () => dispatch => {

    dispatch(clearFiltersResult());
    backend.filterService.findFilters(
        result => dispatch(findFiltersCompleted(result)));

}

const clearFiltersResult = () => ({
    type: actionTypes.CLEAR_FILTER_SEARCH
});

const findFilterCompleted = filterDetails => ({
    type: actionTypes.FIND_FILTER_COMPLETED,
    filterDetails
});

export const findFilterById = (filterId) => dispatch => {
    backend.filterService.findByFilterId(filterId,
        filterDetails => dispatch(findFilterCompleted(filterDetails)));
}


export const getDefaultFilter = () => dispatch => {
    backend.filterService.getDefaultFilter(
        filterDetails => dispatch(findFilterCompleted(filterDetails)));
}

const updateFiltersCompleted = filterList => ({
    type: actionTypes.UPDATE_FILTERS_COMPLETED,
    filterList
});

export const saveFilter = (filterName) => dispatch => {
    backend.filterService.saveFilter(filterName,
        result => dispatch(updateFiltersCompleted(result)));
}

export const clearFilter = () => dispatch => {
    backend.filterService.clearFilter(
        result => dispatch(updateFilterCompleted(result)));
}

export const deleteFilter = (filterId) => dispatch => {
    backend.filterService.deleteFilter(filterId,
        result => dispatch(updateFiltersCompleted(result)));
}

const updateFilterCompleted = filterDetails => ({
    type: actionTypes.UPDATE_FILTER_COMPLETED,
    filterDetails
});

export const updateFilterParam = (filterParamId, filterId, technologyId, mandatory, relevant) => dispatch => {
    backend.filterService.updateFilterParam(filterParamId, filterId, technologyId, mandatory, relevant,
        filterDetails => dispatch(updateFilterCompleted(filterDetails)));
}

export const useProjectAsFilter = (projectId) => dispatch => {
    backend.filterService.useProjectAsFilter(projectId,
        filterDetails => dispatch(findFilterCompleted(filterDetails)))
}

export const useUserAsFilter = (userId) => dispatch => {
    backend.filterService.useUserAsFilter(userId,
        filterDetails => dispatch(findFilterCompleted(filterDetails)))
}
