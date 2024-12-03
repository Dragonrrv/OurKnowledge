import {config, appFetch} from './appFetch';

export const listRelevantTechnologies = (onSuccess) =>
    appFetch(`/technology/list`, config('GET'), onSuccess);

export const addTechnology = (name, parentId,
    onSuccess, onErrors) =>
    appFetch(`/technology/add`,
        config('POST', {name, parentId}), onSuccess, onErrors);

export const deleteTechnology = (technologyId, deleteChildren,
    onSuccess, onErrors) =>
    appFetch(`/technology/delete`,
        config('POST', {technologyId, deleteChildren}), onSuccess, onErrors);
