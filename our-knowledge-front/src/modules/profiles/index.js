import * as actions from './actions';
import reducer from './reducer';
import * as selectors from './selectors';

export {default as Profile} from './details/Profile';
export {default as UpdateProfile} from './update/UpdateProfile';

export default {actions, reducer, selectors};
