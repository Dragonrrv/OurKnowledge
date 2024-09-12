import {useSelector, useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';

import * as selectors from '../selectors';
import * as actions from '../actions';
import {Pager} from '../../common';
import Users from "./Users";

const UsersResult = ({keywords}) => {


    const usersResult = useSelector(selectors.getUsersResult);
    const dispatch = useDispatch();

    if (!usersResult) {
        return <div>Loading...</div>
    }

    if (usersResult.items.length === 0) {
        return (
            <div className="alert alert-danger" role="alert">
                <FormattedMessage id='project.catalog.FindUsersResult.noUsersFound'/>
            </div>
        );
    }
    
    return (

        <div>
            <Users users={usersResult.items}/>
            <Pager 
                back={{
                    enabled: usersResult.page > 1,
                    onClick: () =>
                        dispatch(actions.findUsers(usersResult.page-1, keywords))}}
                next={{
                    enabled: usersResult.existMoreItems,
                    onClick: () =>
                        dispatch(actions.findUsers(usersResult.page+1, keywords))}}/>
        </div>

    );

}

export default UsersResult;
