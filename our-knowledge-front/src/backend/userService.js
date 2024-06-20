import {config, appFetch} from './appFetch';

export const login = (userName, password, onSuccess, onErrors) =>
    appFetch(`/user`,
        config('POST', {userName, password}), onSuccess, onErrors);

export const getProfile = (userId, profileId, onSuccess, onErrors) =>
    appFetch(`/user`,
        config('POST', {userId, profileId}), onSuccess, onErrors);