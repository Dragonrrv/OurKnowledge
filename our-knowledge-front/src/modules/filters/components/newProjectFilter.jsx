import TechnologyFilter from "./TechnologyFilter";
import React, {useEffect, useState} from "react";
import filters from "../index";
import {useDispatch} from "react-redux";
import {FormattedMessage} from "react-intl";
import {useNavigate, useParams} from "react-router-dom";

const newProjectFilter = () => {
    const {filterNameParam} = useParams() || '';

    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [filterName, setFilterName] = useState(filterNameParam);

    useEffect(() => {
        dispatch(filters.actions.getDefaultFilter());
    }, [dispatch]);

    const saveFilter = (event) => {
        event.preventDefault();
        dispatch(filters.actions.saveFilter(filterName));
        navigate(-1);
    }

    return (
        <div>
            <h6><FormattedMessage id='project.filters.filterName'/></h6>
            <input id="keywords" type="text" className="form-control mr-sm-2" style={{marginBottom: '20px', maxWidth: '300px'}}
                   value={filterName} onChange={e => setFilterName(e.target.value)}/>
            <TechnologyFilter/>
            <div style={{marginTop: '20px'}}>
                <button className="btn btn-primary my-2 my-sm-0" onClick={e => saveFilter(e)}>
                    <FormattedMessage id='project.global.buttons.save'/>
                </button>
            </div>
        </div>
    );

}

export default newProjectFilter;