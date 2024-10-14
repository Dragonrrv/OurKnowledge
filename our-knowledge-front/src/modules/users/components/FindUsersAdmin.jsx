import {useDispatch, useSelector} from "react-redux";
import React, {useEffect, useState} from "react";
import {FormattedMessage} from "react-intl";
import users from "../index";
import UsersResult from "./UsersResult";
import {Link, useNavigate} from "react-router-dom";
import filters from "../../filters";
import MoreOptions from "../../filters/components/MoreOptions";
import FilterList from "../../filters/components/FilterList";

const FindUsersAdmin = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();
    const userId = useSelector(users.selectors.getUserId);
    const filterId = useSelector(filters.selectors.getFilterId);
    const [keywords, setKeywords] = useState('');
    const [useFilter, setUseFilter] = useState(false);

    useEffect(() => {
        dispatch(filters.actions.getDefaultFilter(userId));
        dispatch(users.actions.findUsers(1, ''));
    }, [dispatch]);

    const findUsers = event => {
        event.preventDefault();
        setUseFilter(true)
        dispatch(users.actions.findUsers(1, keywords.trim(), filterId));
    }

    const clearFilter = event => {
        event.preventDefault();
        dispatch(filters.actions.clearFilter(userId));
    }

    return (
        <div style={{display: 'flex', justifyContent: 'space-between'}}>
            <div style={{flexGrow: 1}}>
                <div style={{marginBottom: '20px'}}>
                    <h6><FormattedMessage id='project.filters.filterByName'/></h6>
                    <input id="keywords" type="text" className="form-control mr-sm-2" style={{maxWidth: '350px', width: '100%'}}
                           value={keywords} onChange={e => setKeywords(e.target.value)}/>
                </div>
                <MoreOptions/>
                <div style={{display: 'flex'}}>
                    <div className="nav-link" onClick={e => findUsers(e)} style={{cursor: 'pointer', color: 'blue'}} onMouseEnter={e => e.target.style.color = 'darkBlue'}  // Hover color
                         onMouseLeave={e => e.target.style.color = 'blue'}>
                        <FormattedMessage id='project.filters.search'/>
                    </div><div className="nav-link" onClick={e => clearFilter(e)} style={{cursor: 'pointer', color: 'blue'}} onMouseEnter={e => e.target.style.color = 'darkBlue'}  // Hover color
                               onMouseLeave={e => e.target.style.color = 'blue'}>
                    <FormattedMessage id='project.filters.clear'/>
                </div>
                    <Link className="nav-link" to={`/filters/newProjectFilter`}>
                        <FormattedMessage id="project.filters.newFilter"/>
                    </Link>
                </div>
                <UsersResult keywords={keywords} useFilter={useFilter}/>
            </div>
            <FilterList/>
        </div>

    );

}

export default FindUsersAdmin;