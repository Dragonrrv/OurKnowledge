import {useDispatch, useSelector} from 'react-redux';
import {Link, redirect} from 'react-router-dom';
import {FormattedMessage} from 'react-intl';

import users from '../../users';
import keycloak from "../../../oauth/keycloak";

const Header = () => {

    const userRole = useSelector(users.selectors.getUserRole);
    const userId = useSelector(users.selectors.getUserId);

    function handleLogout() {
        users.actions.logout();
        keycloak.logout({ redirectUri: window.location.origin });
    }


return (

        <nav className="navbar navbar-expand-lg navbar-light bg-light border">
            {(() => {
                if (userRole === "Admin") {
                    return (
                        <ul className="navbar-nav mr-auto">
                            <li className="nav-item">
                                <Link className="nav-link" to="/technologies/technologies">
                                    <FormattedMessage id="project.app.Header.technologies"/>
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/projects/findProjectsAdmin">
                                    <FormattedMessage id="project.app.Header.projects"/>
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/projects/findUsersAdmin">
                                    <FormattedMessage id="project.app.Header.users"/>
                                </Link>
                            </li>
                        </ul>
                    );
                } else if (userRole === "Developer") {
                    return (
                        <ul className="navbar-nav mr-auto">
                            <li className="nav-item">
                                <Link className="nav-link" to={`/profiles/profile/${userId}`}>
                                    <FormattedMessage id="project.app.Header.profile"/>
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/projects/findProjectsDeveloper">
                                    <FormattedMessage id="project.app.Header.projects"/>
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/projects/findUsersDeveloper">
                                    <FormattedMessage id="project.app.Header.users"/>
                                </Link>
                            </li>

                        </ul>
                    );
                }
            })()}
            <ul className="navbar-nav">
                <button onClick={handleLogout} style={{background: 'none',
                    border: 'none',
                    color: 'grey',
                    cursor: 'pointer'}}>
                    <FormattedMessage id="project.app.Header.logout"/>
                </button>
            </ul>
        </nav>

    );

};

export default Header;
