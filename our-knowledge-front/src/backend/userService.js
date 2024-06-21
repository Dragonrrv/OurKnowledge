import {config, appFetch} from './appFetch';

export const login = (userName, password, onSuccess, onErrors) =>
    appFetch(`/user/login`,
        config('POST', {userName, password}), onSuccess, onErrors);

export const logout = () => null;

export const getProfile = (userId, profileId, onSuccess, onErrors) =>
    appFetch(`/user`,
        config('POST', {userId, profileId}), onSuccess, onErrors);