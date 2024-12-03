import * as actionTypes from './actionTypes';
import backend from '../../backend';


const profileUpdated = profile => ({
    type: actionTypes.PROFILE_UPDATED,
    profile
})

export const findProfileCompleted = profile => ({
    type: actionTypes.FIND_PROFILE_COMPLETED,
    profile
});

export const findProfile = (userId) => dispatch => {

    dispatch(clearProfile());
    backend.userService.getProfile(userId,
        result => dispatch(findProfileCompleted(result)))
}

const clearProfile = () => ({
    type: actionTypes.CLEAR_PROFILE
});

export const updateKnowledge = (knowledgeId, mainSkill, likeIt, onSuccess,
                                       onErrors) => dispatch =>
    backend.knowledgeService.updateKnowledge(
        knowledgeId, mainSkill, likeIt, profile => {
            dispatch(profileUpdated(profile));
            onSuccess();
        },
        onErrors);

export const addKnowledge = (technologyId, technologyName, parentTechnologyId, onSuccess,
                                       onErrors) => dispatch =>
    backend.knowledgeService.addKnowledge(
        technologyId, technologyName, parentTechnologyId, profile => {
            dispatch(profileUpdated(profile));
            onSuccess();
        },
        onErrors);

export const deleteKnowledge = (knowledgeId, onSuccess,
                                       onErrors) => dispatch =>
    backend.knowledgeService.deleteKnowledge(
        knowledgeId, profile => {
            dispatch(profileUpdated(profile));
            onSuccess();
        },
        onErrors);
