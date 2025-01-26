import PropTypes from 'prop-types';

import {Link} from 'react-router-dom';
import {Chip} from "@mui/material";

const ProjectLink = ({id, name}) => {
    return (
        <Link title={name} to={`/projects/projectDetails/${id}`}>
            {name}
        </Link>
    );

}

ProjectLink.propTypes = {
    id: PropTypes.number.isRequired,
    name: PropTypes.string.isRequired,
};

export default ProjectLink;
