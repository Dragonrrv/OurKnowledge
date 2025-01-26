import {useEffect, useState} from 'react';
import {useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';

import ProjectsResult from '../components/ProjectsResult';
import projects from "../../index";

const FindProjectsDeveloper = () => {

    const dispatch = useDispatch();
    const [keywords, setKeywords] = useState('');

    useEffect(() => {
        dispatch(projects.actions.findProjects(1, keywords));
    }, [dispatch]);

    const handleSubmit = event => {
        event.preventDefault();
        dispatch(projects.actions.findProjects(1, keywords.trim()));
    }

    return (
        <div>
            <form className="form-inline mt-2 mt-md-0" onSubmit={e => handleSubmit(e)}>

                <input id="keywords" type="text" className="form-control mr-sm-2"
                       value={keywords} onChange={e => setKeywords(e.target.value)}/>

                <button type="submit" className="btn btn-primary my-2 my-sm-0">
                    <FormattedMessage id='project.global.buttons.search'/>
                </button>

            </form>
            <ProjectsResult keywords={keywords}/>
        </div>

    );

}

export default FindProjectsDeveloper;
