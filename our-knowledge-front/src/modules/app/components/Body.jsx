import {useSelector} from 'react-redux';
import {Route, Routes} from 'react-router-dom';

import AppGlobalComponents from './AppGlobalComponents';
import Home from './Home';
import users, {FindUsersAdmin, FindUsersDeveloper} from '../../users';
import {Technologies} from "../../technologies";
import {Profile, UpdateProfile} from "../../profiles";
import {FindProjectsDeveloper, FindProjectsAdmin, ProjectDetails, AddProject, UpdateProject} from "../../projects";
import NewProjectFilter from "../../filters/add/newProjectFilter";

const Body = () => {

    const role = useSelector(users.selectors.getUserRole);
    
   return (

        <div style={{width: '90%', margin: '0 auto'}}>
            <br/>
            <AppGlobalComponents/>
            <Routes>
                <Route path="/*" element={<Home/>}/>
                {role==="Admin" && <Route path="/technologies/technologies" element={<Technologies/>}/>}
                {role==="Admin" && <Route path="/projects/findProjectsAdmin" element={<FindProjectsAdmin/>}/>}
                {role==="Admin" && <Route path="/projects/addProject" element={<AddProject/>}/>}
                {role==="Admin" && <Route path="/projects/updateProject" element={<UpdateProject/>}/>}
                {role==="Admin" && <Route path="/users/findUsersAdmin" element={<FindUsersAdmin/>}/>}
                {role==="Admin" && <Route path="/filters/newProjectFilter" element={<NewProjectFilter/>}/>}
                {role==="Admin" && <Route path="/filters/newProjectFilter/:filterNameParam" element={<NewProjectFilter/>}/>}
                {role==="Developer" && <Route path="/projects/findProjectsDeveloper" element={<FindProjectsDeveloper/>}/>}
                {role==="Developer" && <Route path="/users/findUsersDeveloper" element={<FindUsersDeveloper/>}/>}
                {role==="Developer" && <Route path="/profiles/updateProfile" element={<UpdateProfile/>}/>}
                {<Route path="/profiles/profile/:profileId" element={<Profile/>}/>}
                {<Route path="/projects/projectDetails/:id" element={<ProjectDetails/>}/>}
            </Routes>
        </div>

    );

};

export default Body;
