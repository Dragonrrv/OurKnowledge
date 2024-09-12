import {FormattedMessage} from 'react-intl';
import PropTypes from 'prop-types';

import {ProjectLink} from '../../common';
import ProfileLink from "../../common/components/ProfileLink";

const Users = ({users}) => (

    <table className="table table-striped table-hover">

        <thead>
            <tr>
                <th scope="col">
                    <FormattedMessage id='project.global.fields.email'/>
                </th>
                <th scope="col">
                    <FormattedMessage id='project.global.fields.name'/>
                </th>
                <th scope="col">
                    <FormattedMessage id='project.global.fields.startDate'/>
                </th>
            </tr>
        </thead>

        <tbody>
            {users.map(user =>
                <tr key={user.id}>
                    <td><ProfileLink id={user.id} name={user.email}/></td>
                    <td>{user.name}</td>
                    <td>{user.startDate}</td>
                </tr>
            )}
        </tbody>

    </table>

);

Users.propTypes = {
    users: PropTypes.array.isRequired,
};

export default Users;
