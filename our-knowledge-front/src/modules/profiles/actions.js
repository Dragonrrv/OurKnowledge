import * as actionTypes from './actionTypes';
import backend from '../../backend';

const profileUpdated = profile => ({
    type: actionTypes.PROFILE_UPDATED,
    profile
})

const findProfileCompleted = profile => ({
    type: actionTypes.FIND_PROFILE_COMPLETED,
    profile
});

export const findProfile = (userId, profileId) => dispatch => {

    dispatch(clearProfile());
    backend.userService.getProfile(userId, profileId,
        result => dispatch(findProfileCompleted(result)))
}

const clearProfile = () => ({
    type: actionTypes.CLEAR_PROFILE
});

export const addTechnology = (userId, technologyName, parentId, onSuccess,
                                       onErrors) => dispatch =>
    backend.technologyService.addTechnology(userId,
        technologyName, parentId, profile => {
            dispatch(profileUpdated(profile));
            onSuccess();
        },
        onErrors);

export const updateKnowledge = (userId, knowledgeId, mainSkill, likeIt, onSuccess,
                                       onErrors) => dispatch =>
    backend.knowledgeService.updateKnowledge(userId,
        knowledgeId, mainSkill, likeIt, profile => {
            dispatch(profileUpdated(profile));
            onSuccess();
        },
        onErrors);

export const addKnowledge = (userId, technologyId, technologyName, parentTechnologyId, onSuccess,
                                       onErrors) => dispatch =>
    backend.knowledgeService.addKnowledge(userId,
        technologyId, technologyName, parentTechnologyId, profile => {
            dispatch(profileUpdated(profile));
            onSuccess();
        },
        onErrors);

export const deleteKnowledge = (userId, knowledgeId, onSuccess,
                                       onErrors) => dispatch =>
    backend.knowledgeService.deleteKnowledge(userId,
        knowledgeId, profile => {
            dispatch(profileUpdated(profile));
            onSuccess();
        },
        onErrors);
