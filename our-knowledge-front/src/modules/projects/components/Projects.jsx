import {FormattedMessage} from 'react-intl';
import PropTypes from 'prop-types';

import {ProjectLink} from '../../common';

const Projects = ({projects}) => (

    <table className="table table-striped table-hover">

        <thead>
            <tr>
                <th scope="col">
                    <FormattedMessage id='project.global.fields.name'/>
                </th>
                <th scope="col">
                    <FormattedMessage id='project.global.fields.startDate'/>
                </th>
                <th scope="col">
                    <FormattedMessage id='project.global.fields.status'/>
                </th>
            </tr>
        </thead>

        <tbody style={{
            background: 'linear-gradient(to bottom, #F5F3F3 50%, #EDECEC 50%)',
            backgroundSize: '100% 100px' // Ajusta este valor según el grosor de las rayas
        }}>
            {projects.map(project =>
                <tr key={project.id}>
                    <td><ProjectLink id={project.id} name={project.name}/></td>
                    <td>{project.startDate}</td>
                    <td>{project.status}</td>
                </tr>
            )}
        </tbody>

    </table>

);

Projects.propTypes = {
    projects: PropTypes.array.isRequired,
};

export default Projects;
