import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { FormattedMessage } from 'react-intl';

const ProjectForm = ({ nameV, descriptionV, startDateV, statusV, sizeV, buttonV, onClick }) => {

    const [name, setName] = useState(nameV || '');
    const [description, setDescription] = useState(descriptionV || '');
    const [startDate, setStartDate] = useState(startDateV || '');
    const [status, setStatus] = useState(statusV || '');
    const [size, setSize] = useState(sizeV || 0);

    const handleSubmit = () => {
        onClick(name, description, startDate, status, size); // Llama a la función pasada desde `AddProject`
    };

    return (
        <div>
            <h6><FormattedMessage id='project.projects.addProject.name' /></h6>
            <input id="name" type="text" className="form-control mr-sm-2" style={{ width: '700px' }}
                   value={name} onChange={e => setName(e.target.value)} />
            <h6 style={{ marginTop: '10px' }}><FormattedMessage id='project.projects.addProject.description' /></h6>
            <textarea id="description" className="form-control mr-sm-2" style={{ height: '80px', resize: 'vertical' }}
                      value={description} onChange={e => setDescription(e.target.value)} />
            <div style={{ display: 'flex', marginTop: '10px' }}>
                <div style={{ marginRight: '5%' }}>
                    <h6><FormattedMessage id='project.projects.addProject.startDate' /></h6>
                    <input id="startDate" type="date" className="form-control mr-sm-2" style={{ width: '150px' }}
                           value={startDate} onChange={e => setStartDate(e.target.value)} />
                </div>
                <div style={{ marginRight: '5%' }}>
                    <h6><FormattedMessage id='project.projects.addProject.status' /></h6>
                    <input id="status" type="text" className="form-control mr-sm-2" style={{ width: '150px' }}
                           value={status} onChange={e => setStatus(e.target.value)} />
                </div>
                <div style={{ marginRight: '5%' }}>
                    <h6><FormattedMessage id='project.projects.addProject.size' /></h6>
                    <input id="size" type="number" className="form-control mr-sm-2" style={{ width: '70px' }}
                           value={size} onChange={e => setSize(e.target.value)} />
                </div>
            </div>

            <div style={{ marginTop: '30px' }}>
                <button className="btn btn-primary my-2 my-sm-0" onClick={handleSubmit}>
                    {buttonV}
                </button>
            </div>
        </div>
    );
}

ProjectForm.propTypes = {
    nameV: PropTypes.string,
    descriptionV: PropTypes.string,
    startDateV: PropTypes.string,
    statusV: PropTypes.string,
    sizeV: PropTypes.number,
    buttonV: PropTypes.any.isRequired,
    onClick: PropTypes.func.isRequired,
};

export default ProjectForm;