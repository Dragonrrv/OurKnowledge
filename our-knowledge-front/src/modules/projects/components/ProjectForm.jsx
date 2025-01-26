// eslint-disable-next-line no-unused-vars
import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { FormattedMessage } from 'react-intl';
import {Button, Grid2} from "@mui/material";

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
        <Grid2 container spacing={2} sx={{ border: '1px solid black', borderRadius: '8px', padding: '16px' }}>
            <Grid2 item sx={{ width: '100%' }}>
                <h6><FormattedMessage id='project.projects.addProject.name'/></h6>
                <input id="name" type="text" className="form-control mr-sm-2"
                       value={name} onChange={e => setName(e.target.value)}/>
            </Grid2>
            <Grid2 item sx={{ width: '500px' }}>
                <h6><FormattedMessage id='project.projects.addProject.description'/></h6>
                <textarea id="description" className="form-control mr-sm-2" style={{height: '80px', resize: 'vertical'}}
                          value={description} onChange={e => setDescription(e.target.value)}/>
            </Grid2>
            <Grid2 item>
                <h6><FormattedMessage id='project.projects.addProject.startDate'/></h6>
                <input id="startDate" type="date" className="form-control mr-sm-2"
                       value={startDate} onChange={e => setStartDate(e.target.value)}/>
            </Grid2>
            <Grid2 item>
                <h6><FormattedMessage id='project.projects.addProject.size'/></h6>
                <input id="size" type="number" className="form-control mr-sm-2" style={{maxWidth: '100px'}}
                       value={size} onChange={e => setSize(e.target.value)}/>
            </Grid2>
            <Grid2 item>
                <h6><FormattedMessage id='project.projects.addProject.status'/></h6>
                <input id="status" type="text" className="form-control mr-sm-2" style={{maxWidth: '150px'}}
                       value={status} onChange={e => setStatus(e.target.value)}/>
            </Grid2>
            <Grid2 item>
                <Button style={{marginTop: '10px', marginLeft: '25px'}} variant="contained" color="primary"
                        onClick={handleSubmit}
                >
                    {buttonV}
                </Button>
            </Grid2>
        </Grid2>
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