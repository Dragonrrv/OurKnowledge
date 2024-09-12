const getModuleState = state => state.users;

export const getUser = state => 
    getModuleState(state).user;

export const isLoggedIn = state =>
    getUser(state) !== null

export const getUsersResult = state =>
    getModuleState(state).usersResult;

export const getUserId = state =>
    isLoggedIn(state) ? getUser(state).id : null;

export const getUserName = state =>
    isLoggedIn(state) ? getUser(state).name : null;

export const getUserRole = state => 
    isLoggedIn(state) ? getUser(state).role : null;



