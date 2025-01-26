import { useSelector } from 'react-redux';
import { useLocation, useNavigate } from 'react-router-dom';
import { FormattedMessage } from 'react-intl';
import {AppBar, Toolbar, Tabs, Tab, Box} from '@mui/material';

import users from '../../users';
import keycloak from "../../../oauth/keycloak";
import { useState, useEffect } from 'react';

const Header = () => {
    const userRole = useSelector(users.selectors.getUserRole);
    const userId = useSelector(users.selectors.getUserId);
    const location = useLocation();
    const navigate = useNavigate();
    const [selectedTab, setSelectedTab] = useState(false);

    const tabStyle = {
        color: 'lightgrey',
        '&.Mui-selected': { color: 'white' },
        '&:hover': { color: 'white' }
    };

    useEffect(() => {
        switch (location.pathname) {
            case '/technologies/technologies':
            case `/profiles/profile/${userId}`:
                setSelectedTab(0);
                break;
            case '/projects/findProjectsAdmin':
            case '/projects/findProjectsDeveloper':
                setSelectedTab(1);
                break;
            case '/users/findUsersAdmin':
            case '/users/findUsersDeveloper':
                setSelectedTab(2);
                break;
            default:
                setSelectedTab(false);
        }
    }, [location, userId]);

    function handleLogout() {
        users.actions.logout();
        keycloak.logout({ redirectUri: window.location.origin });
    }

    return (
        <AppBar position="static">
            <Toolbar>
                <Tabs
                    value={selectedTab} sx={{width: '100%'}}
                    onChange={(event, newValue) => setSelectedTab(newValue)}
                    TabIndicatorProps={{ style: { backgroundColor: 'white' } }}
                >
                    {userRole === "Admin" && (
                        <Tab label={<FormattedMessage id="project.app.Header.technologies" />}
                             onClick={() => navigate('/technologies/technologies')} value={0}
                             sx={tabStyle} />
                    )}
                    {userRole === "Admin" && (
                        <Tab label={<FormattedMessage id="project.app.Header.projects" />}
                             onClick={() => navigate('/projects/findProjectsAdmin')} value={1}
                             sx={tabStyle} />
                    )}
                    {userRole === "Admin" && (
                        <Tab label={<FormattedMessage id="project.app.Header.users" />}
                             onClick={() => navigate('/users/findUsersAdmin')} value={2}
                             sx={tabStyle} />
                    )}
                    {userRole === "Developer" && (
                        <Tab label={<FormattedMessage id="project.app.Header.profile" />}
                             onClick={() => navigate(`/profiles/profile/${userId}`)} value={0}
                             sx={tabStyle} />
                    )}
                    {userRole === "Developer" && (
                        <Tab label={<FormattedMessage id="project.app.Header.projects" />}
                             onClick={() => navigate('/projects/findProjectsDeveloper')} value={1}
                             sx={tabStyle} />
                    )}
                    {userRole === "Developer" && (
                        <Tab label={<FormattedMessage id="project.app.Header.users" />}
                             onClick={() => navigate('/users/findUsersDeveloper')} value={2}
                             sx={tabStyle} />
                    )}
                    <Box sx={{ flexGrow: 1 }} />
                    <Tab label={<FormattedMessage id="project.app.Header.logout" />} onClick={handleLogout} value={3}
                         sx={{ ...tabStyle}} />
                </Tabs>
            </Toolbar>
        </AppBar>
    );
};

export default Header;