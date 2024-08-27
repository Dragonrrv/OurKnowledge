import {useIntl} from "react-intl";
import * as adminActions from "../../technologies/actions";
import * as developActions from "../../profiles/actions";
import {useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import PropTypes from "prop-types";
import * as selectors from "../../profiles/selectors";
import users from "../../users";

const AddTechnology = ({parentId, relevant, onAdd}) => {

    const intl = useIntl()
    const dispatch = useDispatch();
    const userId = useSelector(users.selectors.getUserId);
    const [newTechnologyName, setNewTechnologyName] = useState('');

    const handleAddTechnology = (event) => {
        event.preventDefault();
        if(relevant){
            dispatch(adminActions.addTechnology(userId, newTechnologyName, parentId));
        } else {
            console.log(parentId);
            dispatch(developActions.addKnowledge(userId, null, newTechnologyName, parentId))
        }
        setNewTechnologyName('');
        onAdd();
    };

    return (
        <div>
            <div style={{marginTop: '4px'}} className="context-menu-title">
                {intl.formatMessage({id: 'project.technologies.technologies.add'})}
            </div>
            <form style={{marginTop: '2px', marginBottom: '0.2px'}} onSubmit={handleAddTechnology} className="context-menu-form">
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
