import {combineReducers} from 'redux';

import app from '../modules/app';
import users from '../modules/users';
import profiles from '../modules/profiles';
import technologies from "../modules/technologies";
import projects from "../modules/projects";

const rootReducer = combineReducers({
    app: app.reducer,
    technologies: technologies.reducer,
    profiles: profiles.reducer,
    projects: projects.reducer,
    users: users.reducer,
});

export default rootReducer;
