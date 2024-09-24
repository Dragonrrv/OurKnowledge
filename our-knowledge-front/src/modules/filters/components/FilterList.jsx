import {useDispatch, useSelector} from "react-redux";
import * as selectors from "../selectors";
import * as actions from "../actions";
import {useEffect} from "react";
import filters from "../index";
import users from "../../users";
import {FormattedMessage, useIntl} from "react-intl";


const FilterList = () => {

    const intl = useIntl();
    const filterList = useSelector(selectors.getFilterList);
    const userId = useSelector(users.selectors.getUserId);
    const dispatch = useDispatch();

    console.log(filterList)

    useEffect(() => {
        dispatch(filters.actions.findFilters(userId));
    }, [dispatch]);

    const handleRemove = (filterId) => {

        if (window.confirm(intl.formatMessage({ id: 'project.filters.filterList.delete.confirmation' }))) {
            dispatch(actions.deleteFilter(filterId));
        }
    };

    if(!filterList){
        return <div><FormattedMessage id='project.filters.filterList.noFilters'/></div>
    }

    return (
        <div style={{ maxWidth: '100px'}}>
            {filterList.map(filter =>
                <div style={{display: 'flex'}}>
                    {filter.name}
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
    )
}

export default FilterList;