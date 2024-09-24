import {useEffect, useState} from 'react';
import {useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';

import ProjectsResult from './ProjectsResult';
import projects from "../index";
import {useNavigate} from "react-router-dom";
import technologies from "../../technologies";
import TechnologyFilter from "../../filters/components/TechnologyFilter";
import FilterList from "../../filters/components/FilterList";

const FindProjectsAdmin = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [keywords, setKeywords] = useState('');

    useEffect(() => {
        dispatch(projects.actions.findProjects(1, keywords));
        dispatch(technologies.actions.findTechnologies());
    }, [dispatch]);

    const handleSubmit = event => {
        event.preventDefault();
        dispatch(projects.actions.findProjects(1, keywords.trim()));
    }

    return (
        <div>
            <div style={{display: 'flex'}}>
                <form style={{flexGrow: 1}} className="form-inline mt-2 mt-md-0" onSubmit={e => handleSubmit(e)}>

                    <input id="keywords" type="text" className="form-control mr-sm-2"
                           value={keywords} onChange={e => setKeywords(e.target.value)}/>

                    <button type="submit" className="btn btn-primary my-2 my-sm-0">
                        <FormattedMessage id='project.global.buttons.search'/>
                    </button>
                </form>
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
            <div style={{display: 'flex', justifyContent: 'space-between'}}>
                <TechnologyFilter/>
                <FilterList/>
            </div>
            <ProjectsResult keywords={keywords}/>
        </div>

    );

}

export default FindProjectsAdmin;
