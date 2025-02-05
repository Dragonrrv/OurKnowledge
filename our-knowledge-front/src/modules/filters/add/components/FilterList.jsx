import {useDispatch, useSelector} from "react-redux";
import * as selectors from "../../selectors";
import * as actions from "../../actions";
import {useEffect} from "react";
import filters from "../../index";
import {FormattedMessage, useIntl} from "react-intl";
import {Box, Chip} from "@mui/material";
import {Delete} from "@mui/icons-material";


const FilterList = () => {

    const intl = useIntl();
    const dispatch = useDispatch();
    const filterList = useSelector(selectors.getFilterList);
    const filterName = useSelector(selectors.getFilterName);

    useEffect(() => {
        dispatch(filters.actions.findFilters());
    }, [dispatch]);

    const useFilter = (filterId) => {
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
        <Box sx={{border: '1px solid black', borderRadius: '8px', padding: '10px', marginLeft: "20px", maxWidth: "200px"}}>
            <h6 style={{marginTop: '20px', marginBottom: '10px'}}><FormattedMessage id="project.global.fields.savedFilters"/></h6>
            {filterList && (
                <Box >
                    {filterList.map(filter =>
                        <Box key={filter.id} sx={{marginTop: "5px"}}>
                            {filter.name === filterName ? (
                                <Chip label={filter.name} title={filter.name}
                                      onDelete={() => handleRemove(filter.id)}
                                      deleteIcon={<Delete />}
                                />
                            ) : (
                                <Chip label={filter.name} title={filter.name}
                                      onClick={() => useFilter(filter.id)}
                                      onDelete={() => handleRemove(filter.id)}
                                      deleteIcon={<Delete />}
                                      variant="outlined"
                                />
                            )}
                        </Box>
                    )}
                </Box>
            )}
            {!filterList && (
                <FormattedMessage id='project.filters.filterList.noFilters'/>
            )}

        </Box>
    )
}

export default FilterList;