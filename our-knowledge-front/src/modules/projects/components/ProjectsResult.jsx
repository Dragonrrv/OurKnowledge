import {useSelector, useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';

import * as selectors from '../selectors';
import * as actions from '../actions';
import {Pager} from '../../common';
import Projects from './Projects';

const ProjectsResult = ({keywords}) => {


    const projectsResult = useSelector(selectors.getProjectsResult);
    const dispatch = useDispatch();

    if (!projectsResult) {
        return <div>Loading...</div>
    }

    if (projectsResult.items.length === 0) {
        return (
            <div className="alert alert-danger" role="alert">
                <FormattedMessage id='project.catalog.FindProjectsResult.noProjectsFound'/>
            </div>
        );
    }
    
    return (

        <div>
            <Projects projects={projectsResult.items}/>
            <Pager 
                back={{
                    enabled: projectsResult.page > 1,
                    onClick: () =>
                        dispatch(actions.findProjects(projectsResult.page-1, keywords))}}
                next={{
                    enabled: projectsResult.existMoreItems,
                    onClick: () =>
                        dispatch(actions.findProjects(projectsResult.page+1, keywords))}}/>
        </div>

    );

}

export default ProjectsResult;