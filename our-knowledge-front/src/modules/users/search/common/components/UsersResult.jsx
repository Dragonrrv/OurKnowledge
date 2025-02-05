import {useSelector, useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';

import * as selectors from '../../../selectors';
import * as actions from '../../../actions';
import {Pager} from '../../../../common';
import filters from "../../../../filters";
import PropTypes from "prop-types";

const UsersResult = ({ UsersType: Users, keywords, useFilter}) => {


    const usersResult = useSelector(selectors.getUsersResult);
    const filterId = useSelector(filters.selectors.getFilterId);
    const dispatch = useDispatch();

    if (!usersResult) {
        return <div>Loading...</div>
    }

    if (usersResult.items.length === 0) {
        return (
            <div className="alert alert-danger" role="alert">
                <FormattedMessage id='project.users.findUsersResult.noUsersFound'/>
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

UsersResult.propTypes = {
    UsersType: PropTypes.elementType.isRequired,
    keywords: PropTypes.string.isRequired,
    useFilter: PropTypes.bool.isRequired,
}

export default UsersResult;
