import {config, appFetch} from './appFetch';

export const addKnowledge = (technologyId, technologyName, parentTechnologyId, onSuccess) =>
    appFetch('/knowledge/add', config('POST', {technologyId, technologyName, parentTechnologyId}), onSuccess);

export const deleteKnowledge = (knowledgeId, onSuccess) =>
    appFetch(`/knowledge/delete`, config('POST', {knowledgeId}), onSuccess);

export const updateKnowledge = (knowledgeId, mainSkill, likeIt, onSuccess) =>
    appFetch(`/knowledge/update`, config('POST', {knowledgeId, mainSkill, likeIt}), onSuccess);
