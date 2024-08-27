import {useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {useNavigate, useParams} from 'react-router-dom';

import * as selectors from '../selectors';
import * as actions from '../actions';
import {BackLink} from '../../common';
import {FormattedMessage} from "react-intl";
import TechnologyTreeList from "./TechnologyTreeList";

const ProjectDetails = () => {
    const {id} = useParams();

    const project = useSelector(selectors.getProject);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    console.log(project)

    useEffect(() => {
        dispatch(actions.findProjectById(id));
    }, [dispatch]);

    if (!project) {
        return <div>Loading...</div>
    }
        
    return (

        <div>

            <BackLink/>
            <div style={{display: 'flex'}}>
                <div  style={{flexGrow: 1}} className="card text-left">
                    <div className="card-body">
                        <h3 className="card-title">{project.project.name}</h3>
                        <p className="card-text">{project.project.description}</p>
                        <p><span style={{fontWeight:'bold'}}><FormattedMessage id="project.global.fields.startDate"/>: </span>{project.project.startDate}</p>
                        <p><span style={{fontWeight:'bold'}}><FormattedMessage style={{fontWeight:'bold'}} id="project.global.fields.status"/>: </span>{project.project.status}</p>
                    </div>
                </div>
                <div style={{marginTop: '20px', marginLeft: '20px', marginRight: '20px'}}>
                    <button className="btn btn-primary my-2 my-sm-0"
                            style={{
                                padding: '20px 40px', /* Relleno interno */
                            }}
                            onClick={() => navigate('/projects/updateProject')}
                    >
                        <FormattedMessage id="project.projects.button.updateProject"/>
                    </button>
                </div>
            </div>

            <div style={{display: 'flex'}}>
                <div  style={{flexGrow: 1}}>
                    <h5><FormattedMessage id="project.projectDetails.usedTechnologies"/>:</h5>
                    <TechnologyTreeList technologyTreeList={project.technologyTreeList} root={true}/>
                </div>
                
            </div>


        </div>

    );

}

export default ProjectDetails;
