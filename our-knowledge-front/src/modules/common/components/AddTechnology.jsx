import {useIntl} from "react-intl";
import * as adminActions from "../../technologies/actions";
import * as developActions from "../../profiles/actions";
import {useState} from "react";
import {useDispatch} from "react-redux";
import PropTypes from "prop-types";
import {Box} from "@mui/material";

const AddTechnology = ({parentId, relevant, onAdd}) => {

    const intl = useIntl()
    const dispatch = useDispatch();
    const [newTechnologyName, setNewTechnologyName] = useState('');

    const handleAddTechnology = (event) => {
        event.preventDefault();
        if(relevant){
            dispatch(adminActions.addTechnology(newTechnologyName, parentId));
        } else {
            dispatch(developActions.addKnowledge(null, newTechnologyName, parentId))
        }
        setNewTechnologyName('');
        onAdd();
    };

    return (
        <Box sx={{marginLeft: "4px"}}>
            <div style={{marginTop: '4px'}} className="context-menu-title">
                {intl.formatMessage({id: 'project.technologies.technologies.add'})}
            </div>
            <form style={{marginTop: '2px', marginBottom: '1px'}} onSubmit={handleAddTechnology} className="context-menu-form">
                <input
                    type="text"
                    value={newTechnologyName}
                    onChange={e => setNewTechnologyName(e.target.value)}
                    placeholder={intl.formatMessage({id: 'project.global.fields.technologyName'})}
                    aria-label="New Technology Name"
                />
                <button type="submit">
                    {intl.formatMessage({id: 'project.global.buttons.add'})}
                </button>
            </form>
        </Box>

    )
}

AddTechnology.propTypes = {
    parentId: PropTypes.number.isRequired,
    relevant: PropTypes.bool.isRequired,
    onAdd: PropTypes.func
}

export default AddTechnology;
