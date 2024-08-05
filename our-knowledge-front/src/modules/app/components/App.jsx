import React, { useState, useEffect } from 'react';
import {useDispatch, useSelector} from 'react-redux';

import Header from './Header';
import Body from './Body';
import Footer from './Footer';
import users from '../../users';
import keycloak from '../../../oauth/keycloak';
import {setServiceToken} from "../../../backend/appFetch";


const App = () => {

    const [isAuthenticated, setIsAuthenticated] = useState(false);

    const dispatch = useDispatch();

    useEffect(() => {
        keycloak.init({ onLoad: 'login-required',
        checkLoginIframe: true,
        pkceMethod: 'S256'
        }).then(authenticated => {
            setServiceToken(keycloak.token);
            dispatch(users.actions.login(keycloak.idTokenParsed.name, keycloak.idTokenParsed.email, keycloak.hasResourceRole("client_admin") ? "Admin" : "Developer"));
            setIsAuthenticated(authenticated);
        });



    }, [dispatch]);

    if(!isAuthenticated){
        return "Loading..."
    }

    return (
        <div>
            <Header/>
            <Body/>
            <Footer/>
        </div>
    );

}

export default App;
