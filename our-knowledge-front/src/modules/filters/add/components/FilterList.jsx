import {useDispatch, useSelector} from "react-redux";
import * as selectors from "../../selectors";
import * as actions from "../../actions";
import React, {useEffect} from "react";
import filters from "../../index";
import users from "../../../users";
import {FormattedMessage, useIntl} from "react-intl";


const FilterList = () => {

    const intl = useIntl();
    const dispatch = useDispatch();
    const filterList = useSelector(selectors.getFilterList);
    const filterName = useSelector(selectors.getFilterName);

    useEffect(() => {
        dispatch(filters.actions.findFilters());
    }, [dispatch]);

    const useFilter = (filterId) => {
        console.log(filterId)
        dispatch(actions.findFilterById(filterId));
    };

    const handleRemove = (filterId) => {

        if (window.confirm(intl.formatMessage({ id: 'project.filters.filterList.delete.confirmation' }))) {
            dispatch(actions.deleteFilter(filterId));
        }
    };

    if(!filterList){
        return <div><FormattedMessage id='project.filters.filterList.noFilters'/></div>
    }

    return (
        <div style={{ backgroundColor: 'lightgrey', width: '200px', padding: '15px', marginLeft: '20px', float: 'right'}}>
            <h6 style={{marginTop: '20px', marginBottom: '10px'}}><FormattedMessage id="project.global.fields.savedFilters"/></h6>
            {filterList && (
                <div>
                    {filterList.map(filter =>
                        <div style={{display: 'flex', marginTop: '5px'}}>
                            {filter.name === filterName && (
                                <button onClick={() => null} className="btn btn-primary my-2 my-sm-0" style={{backgroundColor: 'gray'}}>
                                    {filter.name}
                                </button>
                            )}
                            {filter.name !== filterName && (
                                <button onClick={() => useFilter(filter.id)} className="btn btn-primary my-2 my-sm-0">
                                    {filter.name}
                                </button>
                            )}

                            <button
                                onClick={() => handleRemove(filter.id)}
                                style={{
                                    color: 'white',
                                    marginLeft: '10px',
                                    border: 'none',
                                    background: 'red',
                                    cursor: 'pointer',
                                    borderRadius: '30%',
                                    height: '25px', // Ajusta la altura
                                    lineHeight: '25px', // Ajusta la altura del texto
                                }}
                            >
                                X
                            </button>
                        </div>
                    )}
                </div>
            )}
            {!filterList && (
                <FormattedMessage id='project.filters.filterList.noFilters'/>
            )}

        </div>
    )
}

export default FilterList;