import * as actionTypes from './actionTypes';
import backend from '../../backend';
import {removeServiceToken} from "../../backend/appFetch";


const loginCompleted = authenticatedUser => ({
    type: actionTypes.LOGIN_COMPLETED,
    authenticatedUser
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