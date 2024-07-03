import {useSelector} from 'react-redux';
import {Route, Routes} from 'react-router-dom';

import AppGlobalComponents from './AppGlobalComponents';
import Home from './Home';
import {Login, Logout} from '../../users';
import users from '../../users';
import {Technologies} from "../../administration";
import Profile from "../../developers/components/Profile";
import UpdateProfile from "../../developers/components/UpdateProfile";

const Body = () => {

    const loggedIn = useSelector(users.selectors.isLoggedIn);
    
   return (

        <div className="container">
            <br/>
            <AppGlobalComponents/>
            <Routes>
                <Route path="/*" element={<Home/>}/>
                {loggedIn && <Route path="/administration/technologies" element={<Technologies/>}/>}
                {loggedIn && <Route path="/developers/profile" element={<Profile/>}/>}
                {loggedIn && <Route path="/developers/updateProfile" element={<UpdateProfile/>}/>}
                {loggedIn && <Route path="/users/logout" element={<Logout/>}/>}
                {!loggedIn && <Route path="/users/login" element={<Login/>}/>}
            </Routes>
        </div>

    );

};

export default Body;
