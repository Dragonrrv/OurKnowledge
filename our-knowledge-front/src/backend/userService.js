import {config, appFetch} from './appFetch';

export const size = 4;

export const login = (userName, email, role, onSuccess, onErrors) =>
    appFetch(`/user/login`,
        config('POST', {userName, email, role}), onSuccess, onErrors);

export const findUsers = (page, keywords, filterId, onSuccess) =>
    appFetch(`/user/list`,
        config('POST', {size, page, keywords, filterId}), onSuccess);

export const getProfile = (userId, onSuccess, onErrors) =>
    appFetch(`/user/profile`,
        config('POST', {userId}), onSuccess, onErrors);

export const updateUser = (startDate, onSuccess, onErrors) =>
    appFetch(`/user/update`,
        config('POST', {startDate}), onSuccess, onErrors);