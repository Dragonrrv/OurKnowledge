import PropTypes from 'prop-types';

import {Link} from 'react-router-dom';

const ProfileLink = ({id, name}) => {
    return (
        <Link to={`/profiles/profile/${id}`}>
            {name}
        </Link>
    );

}

ProfileLink.propTypes = {
    id: PropTypes.number.isRequired,
    name: PropTypes.string.isRequired,
};

export default ProfileLink;