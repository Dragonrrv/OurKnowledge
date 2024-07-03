
import PropTypes from 'prop-types';

import KnowledgeTreeList from "./KnowledgeTreeList";
import {useEffect, useState, useRef } from "react";
import * as actions from "../actions";
import {useDispatch} from "react-redux";
import {FormattedMessage, useIntl} from "react-intl";
import AddTechnology from "../../common/components/AddTechnology";
import TechnologyTreeName from "../../common/components/TechnologyTreeName";
import TickBox from "../../common/components/TickBox";

const KnowledgeTree = ({ knowledgeTree }) => {
    const [isOpen, setIsOpen] = useState(true);

    const toggleOpen = () => {
        setIsOpen(!isOpen);
    };

    if(knowledgeTree.parent.knowledgeId==null){
        return null;
    }

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <TechnologyTreeName name={knowledgeTree.parent.name} isOpen={isOpen} hasChildren={knowledgeTree.children.length>0} onClick={toggleOpen}/>
                <div style={{ display: 'flex', gap: '60px' }}>
                    <TickBox ok={knowledgeTree.parent.mainSkill} clickable={false} />
                    <TickBox ok={knowledgeTree.parent.likeIt} clickable={false} />
                </div>
            </div>
            <div style={{ paddingLeft: '2em' }}>
                {isOpen && (
                    <KnowledgeTreeList
                        knowledgeTreeList={knowledgeTree.children}
                        root={false}
                    />
                )}
            </div>
        </div>
    );
}

KnowledgeTree.propTypes = {
    knowledgeTree: PropTypes.shape({
        parent: PropTypes.shape({
            id: PropTypes.number.isRequired,
            name: PropTypes.string.isRequired,
            relevant:  PropTypes.bool.isRequired,
            knowledgeId: PropTypes.number.isRequired,
            likeIt: PropTypes.bool.isRequired,
            mainSkill: PropTypes.bool.isRequired
        }).isRequired,
        children: PropTypes.array.isRequired
    }).isRequired

};

export default KnowledgeTree;
