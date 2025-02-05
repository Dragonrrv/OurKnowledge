import {useEffect, useState} from 'react';
import {useDispatch} from 'react-redux';

import ProjectsResult from '../common/components/ProjectsResult';
import Projects from './components/Projects';
import projects from "../../index";
import {Box} from "@mui/material";

const FindProjectsDeveloper = () => {

    const dispatch = useDispatch();
    const [keywords, setKeywords] = useState('');

    useEffect(() => {
        dispatch(projects.actions.findProjects(1, keywords.trim()));
    }, [dispatch, keywords]);

    return (
        <div>
            <Box sx={{marginBottom: "10px"}}>
                <input id="keywords" type="text" className="form-control mr-sm-2"
                       style={{ maxWidth: '250px'}}
                       value={keywords} onChange={e => setKeywords(e.target.value)}/>
            </Box>
            <ProjectsResult ProjectsType={Projects} keywords={keywords} useFilter={false}/>
        </div>

    );

}

export default FindProjectsDeveloper;
