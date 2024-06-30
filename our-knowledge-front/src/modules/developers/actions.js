import * as actionTypes from './actionTypes';
import backend from '../../backend';
import * as selectors from "./selectors";
import {PROFILE_UPDATED} from "./actionTypes";

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

export const removeTechnology = (userId, technologyId, deleteChildren, onSuccess,
                                       onErrors) => dispatch =>
    backend.technologyService.deleteTechnology(userId,
        technologyId, deleteChildren, profile => {
            dispatch(profileUpdated(profile));
            onSuccess();
        },
        onErrors);
