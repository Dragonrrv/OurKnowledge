import {config, appFetch} from './appFetch';

export const listRelevantTechnologies = (onSuccess) =>
    appFetch(`/technology/list`, config('GET'), onSuccess);

export const addTechnology = (userId, name, parentId,
    onSuccess, onErrors) =>
    appFetch(`/technology/add`,
        config('POST', {userId, name, parentId}), onSuccess, onErrors);

export const deleteTechnology = (userId, technologyId, deleteChildren,
    onSuccess, onErrors) =>
    appFetch(`/technology/delete`,
        config('POST', {userId, technologyId, deleteChildren}), onSuccess, onErrors);
