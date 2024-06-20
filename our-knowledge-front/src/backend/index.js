import {init} from './appFetch';
import * as userService from './userService';
import * as catalogService from './knowledgeService';
import * as shoppingService from './technologyService';

export {default as NetworkError} from "./NetworkError";

export default {init, userService, catalogService, shoppingService};
