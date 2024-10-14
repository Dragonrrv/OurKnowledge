import {useSelector, useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';

import * as selectors from '../selectors';
import * as actions from '../actions';
import {Pager} from '../../common';
import Projects from './Projects';
import filters from "../../filters";

const ProjectsResult = ({keywords, useFilter}) => {


    const projectsResult = useSelector(selectors.getProjectsResult);
    const filterId = useSelector(filters.selectors.getFilterId);
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
                        useFilter
                            ? dispatch(actions.findProjects(projectsResult.page - 1, keywords, filterId))
                            : dispatch(actions.findProjects(projectsResult.page - 1, ''))
            }}
                next={{
                    enabled: projectsResult.existMoreItems,
                    onClick: () =>
                        useFilter
                            ? dispatch(actions.findProjects(projectsResult.page + 1, keywords, filterId))
                            : dispatch(actions.findProjects(projectsResult.page + 1, ''))
            }}/>
        </div>

    );

}

export default ProjectsResult;
