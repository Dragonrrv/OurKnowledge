import {appFetch, config} from "./appFetch";

export const findFilters = (userId, onSuccess) =>
    appFetch(`/filter/list`,
        config('POST', {userId}), onSuccess);

export const findByFilterId = (filterId, onSuccess) =>
    appFetch(`/filter`,
        config('Post', {filterId}), onSuccess);

export const getDefaultFilter = (userId, onSuccess) =>
    appFetch(`/filter/default`,
        config('Post', {userId}), onSuccess);

export const saveFilter = (userId, filterName, onSuccess) =>
    appFetch(`/filter/save`,
        config('Post', {userId, filterName}), onSuccess);

export const deleteFilter = (userId, filterId, onSuccess) =>
    appFetch(`/filter/delete`,
        config('Post', {userId, filterId}), onSuccess);

export const updateFilterParam = (userId, filterParamId, filterId, technologyId, mandatory, recommended, onSuccess) =>
    appFetch(`/filter/filterParam/update`,
        config('Post', {userId, filterParamId, filterId, technologyId, mandatory, recommended}),
        onSuccess);