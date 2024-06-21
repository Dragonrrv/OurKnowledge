import * as actionTypes from './actionTypes';
import backend from '../../backend';


const loginCompleted = authenticatedUser => ({
    type: actionTypes.LOGIN_COMPLETED,
    authenticatedUser
});

export const login = (userName, password, onSuccess, onErrors) => dispatch =>
    backend.userService.login(userName, password,
        authenticatedUser => {
            dispatch(loginCompleted(authenticatedUser));
            onSuccess();
        },
        onErrors,
    );

export const logout = () => {

    backend.userService.logout();

    return {type: actionTypes.LOGOUT};

};