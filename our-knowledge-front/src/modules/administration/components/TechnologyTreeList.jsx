import PropTypes from 'prop-types';

import TechnologyTree from "./TechnologyTree";
import {useEffect, useState} from "react";
import {useDispatch} from "react-redux";
import {FormattedMessage, useIntl} from 'react-intl';

import * as actions from '../actions';

const TechnologyTreeList = ({ technologyTreeList, parentTechnologyId, root}) => {

    const intl = useIntl();
    const dispatch = useDispatch();
    const [newTechnologyName, setNewTechnologyName] = useState('');

    const handleSubmit = event => {
        event.preventDefault();
        dispatch(actions.addTechnology(2, newTechnologyName, parentTechnologyId));
        setNewTechnologyName('');
    };

    const rootStyle = {
        background: 'linear-gradient(to bottom, #F5F3F3 50%, #EDECEC 50%)',
        backgroundSize: '100% 60px' // Ajusta este valor según el grosor de las rayas
    };

    const defaultStyle = {};

    return (
        <div style={root ? rootStyle : defaultStyle}>
            {technologyTreeList.map(technologyTree => (
                <div key={technologyTree.id} onContextMenu={e => e.stopPropagation()}>
                    <TechnologyTree technologyTree={technologyTree}/>
                </div>
            ))}
        </div>
    );
}

TechnologyTreeList.propTypes = {
    technologyTreeList: PropTypes.array.isRequired
}

export default TechnologyTreeList;