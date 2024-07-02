import PropTypes from 'prop-types';

import KnowledgeTree from "./KnowledgeTree";
import {useEffect, useState} from "react";
import {useDispatch} from "react-redux";
import {FormattedMessage, useIntl} from 'react-intl';

import * as actions from '../actions';

const KnowledgeTreeList = ({ knowledgeTreeList, root}) => {

    const intl = useIntl();
    const dispatch = useDispatch();
    const [newTechnologyName, setNewTechnologyName] = useState('');

    const handleSubmit = event => {
        event.preventDefault();
        dispatch(actions.addTechnology(1, newTechnologyName, parentTechnologyId));
        setNewTechnologyName('');
    };

    const rootStyle = {
        background: 'linear-gradient(to bottom, #F5F3F3 50%, #EDECEC 50%)',
        backgroundSize: '100% 60px' // Ajusta este valor según el grosor de las rayas
    };

    const defaultStyle = {};

    return (
        <div style={root ? rootStyle : defaultStyle}>
            {knowledgeTreeList.map(knowledgeTree => (
                <div key={knowledgeTree.id} onContextMenu={e => e.stopPropagation()}>
                    <KnowledgeTree knowledgeTree={knowledgeTree}/>
                </div>
            ))}
        </div>

    )
}

KnowledgeTreeList.propTypes = {
    knowledgeTreeList: PropTypes.array.isRequired
}

export default KnowledgeTreeList;