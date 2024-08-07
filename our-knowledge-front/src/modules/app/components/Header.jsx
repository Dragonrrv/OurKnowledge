import {useDispatch, useSelector} from 'react-redux';
import {Link, redirect} from 'react-router-dom';
import {FormattedMessage} from 'react-intl';

import users from '../../users';
import keycloak from "../../../oauth/keycloak";
import {getServiceToken} from "../../../backend/appFetch";

const Header = () => {

    const userRole = useSelector(users.selectors.getUserRole);
    const userId = useSelector(users.selectors.getUserId);

    function handleLogout() {
        users.actions.logout();
        keycloak.logout({ redirectUri: window.location.origin });
    }


return (

        <nav className="navbar navbar-expand-lg navbar-light bg-light border">
            <ul className="navbar-nav mr-auto">
                {(() => {
                    if (userRole === "Admin") {
                        return (
                            <li className="nav-item">
                                <Link className="nav-link" to="/administration/technologies">
                                    <FormattedMessage id="project.app.Header.technologies"/>
                                </Link>
                            </li>
                        );
                    } else if (userRole === "Developer") {
                        return (
                            <li className="nav-item">
                                <Link className="nav-link" to={`/developers/profile/${userId}`}>
                                    <FormattedMessage id="project.app.Header.profile"/>
                                </Link>
                            </li>
                        );
                    }
                })()}
            </ul>
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
