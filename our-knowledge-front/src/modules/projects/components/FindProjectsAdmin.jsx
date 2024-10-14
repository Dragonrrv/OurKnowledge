import React, {useEffect, useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {FormattedMessage} from 'react-intl';

import ProjectsResult from './ProjectsResult';
import projects from "../index";
import {Link, useNavigate} from "react-router-dom";
import FilterList from "../../filters/components/FilterList";
import MoreOptions from "../../filters/components/MoreOptions";
import filters from "../../filters";
import users from "../../users";

const FindProjectsAdmin = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();
    const userId = useSelector(users.selectors.getUserId);
    const filterId = useSelector(filters.selectors.getFilterId);
    const [keywords, setKeywords] = useState('');
    const [useFilter, setUseFilter] = useState(false);

    useEffect(() => {
        dispatch(filters.actions.getDefaultFilter(userId));
        dispatch(projects.actions.findProjects(1, ''));
    }, [dispatch]);

    const findProjects = event => {
        event.preventDefault();
        setUseFilter(true);
        dispatch(projects.actions.findProjects(1, keywords.trim(), filterId));
    }

    const clearFilter = event => {
        event.preventDefault();
        dispatch(filters.actions.clearFilter(userId));
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
                        <button className="btn btn-primary my-2 my-sm-0"
                                style={{
                                    padding: '20px 40px', /* Relleno interno */
                                }}
                                onClick={() => navigate('/projects/addProject')}
                        >
                            <FormattedMessage id="project.projects.button.addProject"/>
                        </button>
                    </div>
                </div>
                <MoreOptions/>
                <div style={{display: 'flex'}}>
                    <div className="nav-link" onClick={e => findProjects(e)} style={{cursor: 'pointer', color: 'blue'}} onMouseEnter={e => e.target.style.color = 'darkBlue'}  // Hover color
                         onMouseLeave={e => e.target.style.color = 'blue'}>
                        <FormattedMessage id='project.filters.search'/>
                    </div><div className="nav-link" onClick={e => clearFilter(e)} style={{cursor: 'pointer', color: 'blue'}} onMouseEnter={e => e.target.style.color = 'darkBlue'}  // Hover color
                         onMouseLeave={e => e.target.style.color = 'blue'}>
                        <FormattedMessage id='project.filters.clear'/>
                    </div>
                    <Link className="nav-link" to={`/filters/newProjectFilter`}>
                        <FormattedMessage id="project.filters.newFilter"/>
                    </Link>
                </div>
                <ProjectsResult keywords={keywords} useFilter={useFilter}/>
            </div>
            <FilterList/>
        </div>

    );

}

export default FindProjectsAdmin;
