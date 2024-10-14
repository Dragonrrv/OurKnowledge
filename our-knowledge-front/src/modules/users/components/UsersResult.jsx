import {useSelector, useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';

import * as selectors from '../selectors';
import * as actions from '../actions';
import {Pager} from '../../common';
import Users from "./Users";
import filters from "../../filters";

const UsersResult = ({keywords, useFilter}) => {


    const usersResult = useSelector(selectors.getUsersResult);
    const filterId = useSelector(filters.selectors.getFilterId);
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
                        useFilter
                            ? dispatch(actions.findUsers(usersResult.page - 1, keywords, filterId))
                            : dispatch(actions.findUsers(usersResult.page - 1, ''))}}
                next={{
                    enabled: usersResult.existMoreItems,
                    onClick: () =>
                        useFilter
                            ? dispatch(actions.findUsers(usersResult.page + 1, keywords, filterId))
                            : dispatch(actions.findUsers(usersResult.page + 1, ''))}}/>
        </div>

    );

}

export default UsersResult;
