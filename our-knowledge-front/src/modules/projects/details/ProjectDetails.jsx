import {useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {useNavigate, useParams} from 'react-router-dom';

import * as selectors from '../selectors';
import * as actions from '../actions';
import {FormattedMessage} from "react-intl";
import ParticipationList from "./components/ParticipationList";
import users from "../../users";
import UsesTree from "../add/components/UsesTree";
import TreeList from "../../common/components/TreeList";
import filters from "../../filters";
import {Box, Button, Divider} from "@mui/material";
import PropTypes from "prop-types";

const ProjectDetails = () => {
    const {id} = useParams();

    const projectDetails = useSelector(selectors.getProjectDetails);
    const userRole = useSelector(users.selectors.getUserRole);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    useEffect(() => {
        if(id!=='0'){
            dispatch(actions.findProjectById(id));
        }
    }, [dispatch, id]);

    const useAsFilter = () => {
        dispatch(filters.actions.useProjectAsFilter(projectDetails.project.id));
        navigate('/filters/newProjectFilter/' + projectDetails.project.name)
    }

    if (!projectDetails) {
        return <div>Loading...</div>
    }
        
    return (
        <div>
            <div style={{display: 'flex'}}>
                <Box sx={{border: '1px solid black', borderRadius: '8px', padding: '16px', width: '100%'}}>
                    <h3>{projectDetails.project.name}</h3>
                    <p>{projectDetails.project.description}</p>
                    <p><span style={{fontWeight: 'bold'}}>
                        <FormattedMessage id="project.global.fields.startDate"/>: </span>
                        {projectDetails.project.startDate}</p>
                    <p><span style={{fontWeight: 'bold'}}>
                        <FormattedMessage style={{fontWeight: 'bold'}} id="project.global.fields.status"/>: </span>
                        {projectDetails.project.status}
                    </p>
                </Box>
                {userRole === "Admin" && (
                    <div style={{marginLeft: '20px', marginRight: '20px'}}>
                        <div style={{marginTop: '20px'}}>
                            <Button variant="contained" color="primary"
                                    sx={{ padding: '12px 25px'}}
                                    onClick={() => navigate('/projects/updateProject')}>
                                <FormattedMessage id="project.projects.button.updateProject"/>
                            </Button>
                        </div>
                        <div style={{marginTop: '20px'}}>
                            <Button variant="contained" color="primary"
                                    sx={{ padding: '12px 25px'}}
                                    onClick={useAsFilter}>
                                <FormattedMessage id="project.global.buttons.useAsFilter"/>
                            </Button>
                        </div>
                    </div>
                )}
            </div>

            <Box sx={{display: 'flex', marginTop: '10px'}}>
                <Box sx={{flexGrow: 1, border: '1px solid black', borderRadius: '8px', padding: '16px'}}>
                    <h5><FormattedMessage id="project.projectDetails.usedTechnologies"/>:</h5>
                    <TreeList treeType={UsesTree} treeList={projectDetails.usesTreeList} dept={0} />
                </Box>
                <Divider orientation="vertical" flexItem sx={{marginLeft: '10px'}} />
                <Box sx={{flexGrow: 2, border: '1px solid black', borderRadius: '8px', padding: '16px'}}>
                    <h5><FormattedMessage id="project.projectDetails.participatedDevelopers"/>:</h5>
                    <ParticipationList participationList={projectDetails.participationList} projectId={projectDetails.project.id}/>
                </Box>
            </Box>
        </div>

    );

}

ProjectDetails.propTypes = {
    id: PropTypes.string.isRequired,
    projectDetails: PropTypes.shape({
        project: PropTypes.shape({
            id: PropTypes.number.isRequired,
            name: PropTypes.string.isRequired,
            description: PropTypes.string.isRequired,
            startDate: PropTypes.string.isRequired,
            status: PropTypes.string.isRequired,
        }).isRequired,
        usesTreeList: PropTypes.arrayOf(PropTypes.object).isRequired,
        participationList: PropTypes.arrayOf(PropTypes.object).isRequired,
    }).isRequired
};

export default ProjectDetails;
