import {combineReducers} from 'redux';

import app from '../modules/app';
import users from '../modules/users';
import developers from '../modules/developers';
import administration from "../modules/administration";

const rootReducer = combineReducers({
    app: app.reducer,
    administration: administration.reducer,
    developers: developers.reducer,
    users: users.reducer,
});

export default rootReducer;
