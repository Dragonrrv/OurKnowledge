import {config, appFetch} from './appFetch';

export const login = (userName, email, role, onSuccess, onErrors) =>
    appFetch(`/user/login`,
        config('POST', {userName, email, role}), onSuccess, onErrors);

export const getProfile = (userId, profileId, onSuccess, onErrors) =>
    appFetch(`/user/profile`,
        config('POST', {userId, profileId}), onSuccess, onErrors);