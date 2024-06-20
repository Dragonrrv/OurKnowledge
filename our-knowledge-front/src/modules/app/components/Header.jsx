import {useSelector} from 'react-redux';
import {Link} from 'react-router-dom';
import {FormattedMessage} from 'react-intl';

import users from '../../users';

const Header = () => {

    const userName = useSelector(users.selectors.getUserName);
    
    return (

        <nav className="navbar navbar-expand-lg navbar-light bg-light border">
            <Link className="navbar-brand" to="/">Our Knowledge</Link>
            <button className="navbar-toggler" type="button" 
                data-toggle="collapse" data-target="#navbarSupportedContent" 
                aria-controls="navbarSupportedContent" aria-expanded="false" 
                aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>

            <div className="collapse navbar-collapse" id="navbarSupportedContent">

                <ul className="navbar-nav mr-auto">
                </ul>


                {userName ?

                <ul className="navbar-nav">

                    <li className="nav-item">
                        <Link className="nav-link" to="/user/profile">
                            <FormattedMessage id="project.users.Login.title"/>
                        </Link>
                    </li>

                    <li>
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

            </div>
        </nav>

    );

};

export default Header;
