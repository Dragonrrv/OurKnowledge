import {useSelector} from 'react-redux';
import {Route, Routes} from 'react-router-dom';

import AppGlobalComponents from './AppGlobalComponents';
import Home from './Home';
import users from '../../users';
import {Technologies} from "../../technologies";
import Profile from "../../profiles/components/Profile";
import UpdateProfile from "../../profiles/components/UpdateProfile";
import {FindProjectsDeveloper, ProjectDetails} from "../../projects";

const Body = () => {

    const loggedIn = useSelector(users.selectors.isLoggedIn);
    
   return (

        <div className="container">
            <br/>
            <AppGlobalComponents/>
            <Routes>
                <Route path="/*" element={<Home/>}/>
                {loggedIn && <Route path="/technologies/technologies" element={<Technologies/>}/>}
                {loggedIn && <Route path="/profiles/profile/:profileId" element={<Profile/>}/>}
                {loggedIn && <Route path="/profiles/updateProfile" element={<UpdateProfile/>}/>}
                {loggedIn && <Route path="/projects/findProjectsDeveloper" element={<FindProjectsDeveloper/>}/>}
                {loggedIn && <Route path="/projects/projectDetails/:id" element={<ProjectDetails/>}/>}
            </Routes>
        </div>

    );

};

export default Body;
