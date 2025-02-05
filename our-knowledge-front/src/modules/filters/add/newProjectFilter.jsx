import TechnologyFilter from "../components/TechnologyFilter";
import {useEffect, useState} from "react";
import filters from "../index";
import {useDispatch} from "react-redux";
import {FormattedMessage} from "react-intl";
import {useNavigate, useParams} from "react-router-dom";
import {Button} from "@mui/material";

const newProjectFilter = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [filterName, setFilterName] = useState(useParams().filterNameParam || '');

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
                <Button variant="contained" onClick={saveFilter}>
                    <FormattedMessage id='project.global.buttons.save'/>
                </Button>
            </div>
        </div>
    );

}

export default newProjectFilter;