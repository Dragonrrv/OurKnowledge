import {useIntl} from "react-intl";
import * as actions from "../../administration/actions";
import {useState} from "react";
import {useDispatch} from "react-redux";
import PropTypes from "prop-types";
import TechnologyTree from "../../administration/components/TechnologyTree";
import Technologies from "../../administration/components/Technologies";
import ErrorDialog from "./ErrorDialog";

const AddTechnology = ({parentTechnologyId, onAdd}) => {

    const intl = useIntl()
    const dispatch = useDispatch();
    const [newTechnologyName, setNewTechnologyName] = useState('');

    const handleAddTechnology = (event) => {
        event.preventDefault();
        dispatch(actions.addTechnology(2, newTechnologyName, parentTechnologyId));
        setNewTechnologyName('');
        onAdd();
    };

    return (
        <div>
            <div className="context-menu-title">
                {intl.formatMessage({id: 'project.administration.technologies.add'})}
            </div>
            <form onSubmit={handleAddTechnology} className="context-menu-form">
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
        </div>

    )
}

AddTechnology.propTypes = {
    onAdd: PropTypes.func.isRequired
}

export default AddTechnology;
