import {config, appFetch} from './appFetch';

export const size = 4;

export const findProjects = (page, keywords, onSuccess) =>
    appFetch(`/project/list`,
        config('POST', {size, page, keywords}), onSuccess);

export const findByProjectId = (id, onSuccess) =>
    appFetch(`/project`,
        config('Post', {id}), onSuccess);
