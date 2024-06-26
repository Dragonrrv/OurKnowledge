import {useSelector} from 'react-redux';
import {Link} from 'react-router-dom';
import {FormattedMessage} from 'react-intl';

import users from '../../users';

const Header = () => {

    const userRole = useSelector(users.selectors.getUserRole);
    
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
                                <Link className="nav-link" to="/user/profile">
                                    <FormattedMessage id="project.app.Header.profile"/>
                                </Link>
                            </li>
                        );
                    }
                })()}
            </ul>
            {userRole ?
                <ul className="navbar-nav">
                    <li className="nav-item">
                        <Link className="nav-link" to="/user/logout">
                            <FormattedMessage id="project.app.Header.logout"/>
                        </Link>
                    </li>
                </ul>
            :
                <ul className="navbar-nav">
                    <li className="nav-item">
                        <Link className="nav-link" to="/users/login">
                            <FormattedMessage id="project.users.Login.title"/>
                        </Link>
                    </li>
                </ul>
            }


        </nav>

    );

};

export default Header;
