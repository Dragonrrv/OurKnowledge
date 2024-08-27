import {init} from './appFetch';
import * as userService from './userService';
import * as knowledgeService from './knowledgeService';
import * as technologyService from './technologyService';
import * as projectService from './projectService';

export {default as NetworkError} from "./NetworkError";

export default {init, userService, knowledgeService, technologyService, projectService};
