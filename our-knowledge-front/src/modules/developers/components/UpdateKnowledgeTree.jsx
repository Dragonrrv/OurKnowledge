
import PropTypes from 'prop-types';

import KnowledgeTreeList from "./KnowledgeTreeList";
import {useEffect, useState, useRef } from "react";
import * as actions from "../actions";
import {useDispatch} from "react-redux";
import {FormattedMessage, useIntl} from "react-intl";
import AddTechnology from "../../common/components/AddTechnology";
import {updateKnowledge} from "../../../backend/knowledgeService";
import KnowledgeTree from "./KnowledgeTree";
import TechnologyTreeName from "../../common/components/TechnologyTreeName";
import TickBox from "../../common/components/TickBox";
import UpdateKnowledgeTreeList from "./UpdateKnowledgeTreeList";

const UpdateKnowledgeTree = ({ knowledgeTree }) => {

    const dispatch = useDispatch();

    const [isOpen, setIsOpen] = useState(true);

    const toggleOpen = () => {
        setIsOpen(!isOpen);
    };

    const changeKnowledge = (known, technologyId, knowledgeId) => {
        if(known){
            dispatch(actions.deleteKnowledge(1, knowledgeId));
        } else {
            dispatch(actions.addTechnology(1, technologyId));
        }
    }

    const updateMainSkill = (before, id) => {
        dispatch(actions.updateKnowledge(1, id, !before, null));

    }

    const updateLikeIt = (before, id) => {
        dispatch(actions.updateKnowledge(1, id, null, !before));
    }

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <TechnologyTreeName name={knowledgeTree.parent.name} isOpen={isOpen} hasChildren={knowledgeTree.children.length>0} onClick={toggleOpen}/>
                <div style={{ display: 'flex', gap: '60px' }}>
                    <div style={{marginRight : knowledgeTree.parent.knowledgeId==null ? '160px' : '0px'}}>
                        <TickBox ok={knowledgeTree.parent.knowledgeId!=null} clickable={true}
                                 onClick={() => changeKnowledge(knowledgeTree.parent.knowledgeId!=null, knowledgeTree.parent.id, KnowledgeTree.parent.knowledgeId)}/>
                    </div>
                    {knowledgeTree.parent.knowledgeId!=null &&
                        <TickBox ok={knowledgeTree.parent.mainSkill} clickable={true}
                                 onClick={() => updateMainSkill(knowledgeTree.parent.mainSkill, KnowledgeTree.parent.knowledgeId)}/>
                    }
                    {knowledgeTree.parent.knowledgeId != null &&
                    <TickBox ok={knowledgeTree.parent.likeIt} clickable={true}
                             onClick={() => updateLikeIt(knowledgeTree.parent.likeIt, KnowledgeTree.parent.knowledgeId)}/>
                    }
                </div>
            </div>
            <div style={{ paddingLeft: '2em' }}>
                {isOpen && (
                    <UpdateKnowledgeTreeList
                        knowledgeTreeList={knowledgeTree.children}
                        root={false}
                    />
                )}
            </div>
        </div>
    );
}

UpdateKnowledgeTree.propTypes = {
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

export default UpdateKnowledgeTree;
