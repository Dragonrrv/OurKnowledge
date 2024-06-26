import {combineReducers} from 'redux';

import app from '../modules/app';
import users from '../modules/users';
import administration from "../modules/administration";

const rootReducer = combineReducers({
    app: app.reducer,
    administration: administration.reducer,
    users: users.reducer,
});

export default rootReducer;
