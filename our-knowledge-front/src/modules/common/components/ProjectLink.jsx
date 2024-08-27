import PropTypes from 'prop-types';

import {Link} from 'react-router-dom';

const ProjectLink = ({id, name}) => {
    console.log(id)
    return (
        <Link to={`/projects/projectDetails/${id}`}>
            {name}
        </Link>
    );

}

ProjectLink.propTypes = {
    id: PropTypes.number.isRequired,
    name: PropTypes.string.isRequired,
};

export default ProjectLink;
