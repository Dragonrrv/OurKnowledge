import {config, appFetch} from './appFetch';

export const size = 4;

export const findProjects = (page, keywords, onSuccess) =>
    appFetch(`/project/list`,
        config('POST', {size, page, keywords}), onSuccess);

export const findByProjectId = (id, onSuccess) =>
    appFetch(`/project`,
        config('Post', {id}), onSuccess);

export const addProject = (name, description, startDate, status, size, technologyIdList, onSuccess) =>
    appFetch(`/project/add`,
        config('Post', {name, description, startDate, status, size, technologyIdList}), onSuccess);

export const updateProject = (id, name, description, startDate, status, size, updateTechnologies, technologyIdList, onSuccess) =>
    appFetch(`/project/update`,
        config('Post', {id, name, description, startDate, status, size, updateTechnologies, technologyIdList}), onSuccess);

export const addUses = (projectId, technologyId, onSuccess) =>
    appFetch(`/project/uses/add`,
        config('Post', {projectId, technologyId}), onSuccess);

export const deleteUses = (usesId, onSuccess) =>
    appFetch(`/project/uses/delete`,
        config('Post', {usesId}), onSuccess);

