import {combineReducers} from 'redux';

import app from '../modules/app';
import users from '../modules/users';
import profiles from '../modules/profiles';
import technologies from "../modules/technologies";
import projects from "../modules/projects";
import filters from "../modules/filters";

const rootReducer = combineReducers({
    app: app.reducer,
    users: users.reducer,
    technologies: technologies.reducer,
    profiles: profiles.reducer,
    projects: projects.reducer,
    filters: filters.reducer,
});

export default rootReducer;
