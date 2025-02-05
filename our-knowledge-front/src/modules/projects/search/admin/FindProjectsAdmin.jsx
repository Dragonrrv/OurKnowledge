import {useEffect, useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {FormattedMessage} from 'react-intl';

import ProjectsResult from '../common/components/ProjectsResult';
import Projects from './components/Projects';
import projects from "../../index";
import {Link} from "react-router-dom";
import FilterList from "../../../filters/add/components/FilterList";
import MoreOptions from "../../../filters/add/components/MoreOptions";
import filters from "../../../filters";
import {Button, ButtonGroup} from "@mui/material";

const FindProjectsAdmin = () => {

    const dispatch = useDispatch();
    const filterId = useSelector(filters.selectors.getFilterId);
    const [keywords, setKeywords] = useState('');
    const [useFilter, setUseFilter] = useState(false);

    useEffect(() => {
        dispatch(filters.actions.getDefaultFilter());
        dispatch(projects.actions.findProjects(1, ''));
    }, [dispatch]);

    const findProjects = event => {
        event.preventDefault();
        setUseFilter(true);
        dispatch(projects.actions.findProjects(1, keywords.trim(), filterId));
    }

    const clearFilter = event => {
        event.preventDefault();
        dispatch(filters.actions.clearFilter());
    }

    return (
        <div style={{display: 'flex', justifyContent: 'space-between'}}>
            <div style={{flexGrow: 1}}>
                <div style={{display: 'flex', justifyContent: 'space-between'}}>
                    <div>
                        <h6><FormattedMessage id='project.filters.filterByName'/></h6>
                        <input id="keywords" type="text" className="form-control mr-sm-2" style={{maxWidth: '350px', width: '100%'}}
                               value={keywords} onChange={e => setKeywords(e.target.value)}/>
                    </div>
                    <div style={{marginTop: '20px', marginLeft: '20px', marginRight: '20px'}}>
                        <Button  component={Link} to={`/projects/addProject`} variant="contained"
                                style={{
                                    padding: '20px 40px', /* Relleno interno */
                                }}>
                            <FormattedMessage id="project.projects.button.addProject"/>
                        </Button>
                    </div>
                </div>
                <MoreOptions/>
                <ButtonGroup variant="text" aria-label="Basic button group">
                    <Button onClick={e => findProjects(e)}>
                        <FormattedMessage id='project.filters.search' />
                    </Button>
                    <Button onClick={e => clearFilter(e)}>
                        <FormattedMessage id='project.filters.clear' />
                    </Button>
                    <Button component={Link} to={`/filters/newProjectFilter`}>
                        <FormattedMessage id="project.filters.newFilter" />
                    </Button>
                </ButtonGroup>
                <ProjectsResult ProjectsType={Projects} keywords={keywords} useFilter={useFilter}/>
            </div>
            <FilterList/>
        </div>

    );

}

export default FindProjectsAdmin;
