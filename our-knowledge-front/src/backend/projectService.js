import {config, appFetch} from './appFetch';

export const size = 4;

export const findProjects = (page, keywords, filterId, onSuccess) =>
    appFetch(`/project/list`,
        config('POST', {size, page, keywords, filterId}), onSuccess);

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

export const addParticipation = (userId, projectId, startDate, endDate, onSuccess) =>
    appFetch(`/project/participation/add`,
        config('Post', {userId, projectId, startDate, endDate}), onSuccess);

export const updateParticipation = (participationId, startDate, endDate, onSuccess) =>
    appFetch(`/project/participation/update`,
        config('Post', {participationId, startDate, endDate}), onSuccess);

export const addVerification = (userId, usesId, onSuccess) =>
    appFetch(`/project/verification/add`,
        config('Post', {userId, usesId}), onSuccess);

export const deleteVerification = (verificationId, deleteKnowledge, onSuccess) =>
    appFetch(`/project/verification/delete`,
        config('Post', {verificationId, deleteKnowledge}), onSuccess);


