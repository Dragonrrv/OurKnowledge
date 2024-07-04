
import PropTypes from 'prop-types';

import {useEffect, useState, useRef } from "react";
import * as actions from "../actions";
import {useDispatch} from "react-redux";
import AddTechnology from "../../common/components/AddTechnology";
import TechnologyTreeName from "../../common/components/TechnologyTreeName";
import TickBox from "../../common/components/TickBox";
import UpdateKnowledgeTreeList from "./UpdateKnowledgeTreeList";

const UpdateKnowledgeTree = ({ knowledgeTree }) => {

    const dispatch = useDispatch();

    const [isOpen, setIsOpen] = useState(true);
    const [showAddMenu, setShowAddMenu] = useState(false);
    const [addMenuPosition, setAddMenuPosition] = useState({ x: 0, y: 0 });
    const contextMenuRef = useRef(null);

    const toggleOpen = () => {
        setIsOpen(!isOpen);
    };

    const handleAddMenu = (event) => {
        event.preventDefault();
        setAddMenuPosition({ x: event.clientX, y: event.clientY });
        if (showAddMenu) {
            setShowAddMenu(false);
        } else {
            setShowAddMenu(true);
        }
    };

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (contextMenuRef.current && !contextMenuRef.current.contains(event.target)) {
                setShowAddMenu(!showAddMenu);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);

        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const changeKnowledge = (known, technologyId, knowledgeId) => {
        if(known){
            dispatch(actions.deleteKnowledge(1, knowledgeId));
        } else {
            dispatch(actions.addKnowledge(1, technologyId));
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
            <div onContextMenu={handleAddMenu}  style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <TechnologyTreeName name={knowledgeTree.parent.name} isOpen={isOpen} hasChildren={knowledgeTree.children.length>0} onClick={toggleOpen}/>
                <div style={{ display: 'flex', gap: '60px' }}>
                    <div style={{marginRight : knowledgeTree.parent.knowledgeId==null ? '160px' : '0px'}}>
                        <TickBox ok={knowledgeTree.parent.knowledgeId!=null} clickable={true}
                                 onClick={() => changeKnowledge(knowledgeTree.parent.knowledgeId!=null, knowledgeTree.parent.id, knowledgeTree.parent.knowledgeId)}/>
                    </div>
                    {knowledgeTree.parent.knowledgeId!=null &&
                        <TickBox ok={knowledgeTree.parent.mainSkill} clickable={true}
                                 onClick={() => updateMainSkill(knowledgeTree.parent.mainSkill, knowledgeTree.parent.knowledgeId)}/>
                    }
                    {knowledgeTree.parent.knowledgeId != null &&
                    <TickBox ok={knowledgeTree.parent.likeIt} clickable={true}
                             onClick={() => updateLikeIt(knowledgeTree.parent.likeIt, knowledgeTree.parent.knowledgeId)}/>
                    }
                </div>
            </div>
            {showAddMenu && (
                <div
                    className="context-menu"
                    style={{
                        top: addMenuPosition.y,
                        left: addMenuPosition.x,
                        paddingLeft: '2em'
                    }}
                    ref={contextMenuRef}
                >
                    <AddTechnology parentId={knowledgeTree.parent.id}
                                   userId={1}
                                   relevant={false}
                                   onAdd = {() =>setShowAddMenu(!showAddMenu)}/>
                </div>
            )}
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
            knowledgeId: PropTypes.number,
            likeIt: PropTypes.bool,
            mainSkill: PropTypes.bool
        }).isRequired,
        children: PropTypes.array.isRequired
    }).isRequired

};

export default UpdateKnowledgeTree;
