import * as actionTypes from './actionTypes';
import backend from '../../backend';
import {removeServiceToken} from "../../backend/appFetch";
import profiles from "../profiles";


const loginCompleted = authenticatedUser => ({
    type: actionTypes.LOGIN_COMPLETED,
    authenticatedUser
});

const findUsersCompleted = usersResult => ({
    type: actionTypes.FIND_USERS_COMPLETED,
    usersResult
});

export const findUsers = (page, keywords, filterId) => dispatch => {

    dispatch(clearUsersResult());
    backend.userService.findUsers(page, keywords, filterId,
        result => dispatch(findUsersCompleted(result)));
}

export const updateUser = (startDate) => dispatch => {

    dispatch(clearUsersResult());
    backend.userService.updateUser(startDate,
        result => dispatch(profiles.actions.findProfileCompleted(result)));
}

const clearUsersResult = () => ({
    type: actionTypes.CLEAR_PROJECT_SEARCH
});

export const login = (userName, email, role, onSuccess, onErrors) => dispatch =>
    backend.userService.login(userName, email, role,
        authenticatedUser => {
            dispatch(loginCompleted(authenticatedUser));
            onSuccess();
        },
        onErrors,
    );

export const logout = () => {

    removeServiceToken();

    return {type: actionTypes.LOGOUT};

};