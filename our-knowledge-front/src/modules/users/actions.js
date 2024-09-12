import * as actionTypes from './actionTypes';
import backend from '../../backend';
import {removeServiceToken} from "../../backend/appFetch";


const loginCompleted = authenticatedUser => ({
    type: actionTypes.LOGIN_COMPLETED,
    authenticatedUser
});

const findUsersCompleted = usersResult => ({
    type: actionTypes.FIND_USERS_COMPLETED,
    usersResult
});

export const findUsers = (page, keywords) => dispatch => {

    dispatch(clearUsersResult());
    backend.userService.findUsers(page, keywords,
        result => dispatch(findUsersCompleted(result)));
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