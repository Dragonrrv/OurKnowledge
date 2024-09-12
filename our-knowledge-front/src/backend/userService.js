import {config, appFetch} from './appFetch';

export const size = 4;

export const login = (userName, email, role, onSuccess, onErrors) =>
    appFetch(`/user/login`,
        config('POST', {userName, email, role}), onSuccess, onErrors);

export const findUsers = (page, keywords, onSuccess) =>
    appFetch(`/user/list`,
        config('POST', {size, page, keywords}), onSuccess);

export const getProfile = (userId, profileId, onSuccess, onErrors) =>
    appFetch(`/user/profile`,
        config('POST', {userId, profileId}), onSuccess, onErrors);