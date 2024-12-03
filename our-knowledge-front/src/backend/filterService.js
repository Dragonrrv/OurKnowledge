import {appFetch, config} from "./appFetch";

export const findFilters = (onSuccess) =>
    appFetch(`/filter/list`,
        config('POST', {}), onSuccess);

export const findByFilterId = (filterId, onSuccess) =>
    appFetch(`/filter`,
        config('Post', {filterId}), onSuccess);

export const getDefaultFilter = (onSuccess) =>
    appFetch(`/filter/default`,
        config('Post', {}), onSuccess);

export const saveFilter = (filterName, onSuccess) =>
    appFetch(`/filter/save`,
        config('Post', {filterName}), onSuccess);

export const clearFilter = (onSuccess) =>
    appFetch(`/filter/clear`,
        config('Post', {}), onSuccess);

export const deleteFilter = (filterId, onSuccess) =>
    appFetch(`/filter/delete`,
        config('Post', {filterId}), onSuccess);

export const updateFilterParam = (filterParamId, filterId, technologyId, mandatory, recommended, onSuccess) =>
    appFetch(`/filter/filterParam/update`,
        config('Post', {filterParamId, filterId, technologyId, mandatory, recommended}),
        onSuccess);

export const useProjectAsFilter = (projectId, onSuccess) =>
    appFetch(`/filter/createByProject`,
        config('Post', {projectId}),
        onSuccess);

export const useUserAsFilter = (userId, onSuccess) =>
    appFetch(`/filter/createByUser`,
        config('Post', {userId}),
        onSuccess);