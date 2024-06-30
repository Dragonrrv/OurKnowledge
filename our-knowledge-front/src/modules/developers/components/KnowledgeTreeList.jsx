import PropTypes from 'prop-types';

import KnowledgeTree from "./KnowledgeTree";
import {useEffect, useState} from "react";
import {useDispatch} from "react-redux";
import {FormattedMessage, useIntl} from 'react-intl';

import * as actions from '../actions';

const KnowledgeTreeList = ({ knowledgeTreeList}) => {

    const intl = useIntl();
    const dispatch = useDispatch();
    const [newTechnologyName, setNewTechnologyName] = useState('');

    const handleSubmit = event => {
        event.preventDefault();
        dispatch(actions.addTechnology(1, newTechnologyName, parentTechnologyId));
        setNewTechnologyName('');
    };
    let pos
    return(

        <div>
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