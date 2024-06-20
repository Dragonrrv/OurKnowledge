import {config, appFetch} from './appFetch';

export const addKnowledge = (userId, technologyId, onSuccess) =>
    appFetch('/knowledge/add', config('POST', {userId, technologyId}), onSuccess);

export const deleteKnowledge = (userId, knowledgeId, onSuccess) =>
    appFetch(`/knowledge/delete`, config('POST', {userId, knowledgeId}), onSuccess);

export const updateKnowledge = (userId, knowledgeId, mainSkill, likeIt, onSuccess) =>
    appFetch(`/knowledge/update`, config('POST', {userId, knowledgeId, mainSkill, likeIt}), onSuccess);
