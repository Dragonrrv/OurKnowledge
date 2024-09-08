
import PropTypes from 'prop-types';

import {useEffect, useState, useRef } from "react";
import * as actions from "../actions";
import {useDispatch, useSelector} from "react-redux";
import AddTechnology from "../../common/components/AddTechnology";
import TechnologyTreeName from "../../common/components/TechnologyTreeName";
import TickBox from "../../common/components/TickBox";
import users from "../../users";
import TreeList from "../../common/components/TreeList";
import {useIntl} from "react-intl";

const UpdateKnowledgeTree = ({tree, dept}) => {

    const dispatch = useDispatch();
    const intl = useIntl();

    const userId = useSelector(users.selectors.getUserId);

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
            if (window.confirm(intl.formatMessage({ id:'project.profile.updateProfile.deleteKnowledge.explanation'}))) {
                dispatch(actions.deleteKnowledge(userId, knowledgeId));
            }
        } else {
            dispatch(actions.addKnowledge(userId, technologyId));
        }
    }

    const updateMainSkill = (before, id) => {
        dispatch(actions.updateKnowledge(userId, id, !before, null));

    }

    const updateLikeIt = (before, id) => {
        dispatch(actions.updateKnowledge(userId, id, null, !before));
    }

    return (
        <div>
            <div onContextMenu={handleAddMenu}  style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div style={{paddingLeft: 2*dept+'em'}}>
                    <TechnologyTreeName name={tree.parent.name} isOpen={isOpen} hasChildren={tree.children.length>0} onClick={toggleOpen}/>
                </div>
                <div style={{ display: 'flex', gap: '60px' }}>
                    <div style={{marginRight : tree.parent.knowledgeId==null ? '160px' : '0px'}}>
                        <TickBox ok={tree.parent.knowledgeId!=null} clickable={true}
                                 onClick={() => changeKnowledge(tree.parent.knowledgeId!=null, tree.parent.id, tree.parent.knowledgeId)}/>
                    </div>
                    {tree.parent.knowledgeId!=null &&
                        <TickBox ok={tree.parent.mainSkill} clickable={true}
                                 onClick={() => updateMainSkill(tree.parent.mainSkill, tree.parent.knowledgeId)}/>
                    }
                    {tree.parent.knowledgeId != null &&
                    <TickBox ok={tree.parent.likeIt} clickable={true}
                             onClick={() => updateLikeIt(tree.parent.likeIt, tree.parent.knowledgeId)}/>
                    }
                </div>
            </div>
            {showAddMenu && (
                <div
                    className="context-menu"
                    style={{
                        top: addMenuPosition.y,
                        left: addMenuPosition.x,
                        paddingLeft: 2*(dept+1)+'em'
                    }}
                    ref={contextMenuRef}
                >
                    <AddTechnology parentId={tree.parent.id}
                                   relevant={false}
                                   onAdd = {() =>setShowAddMenu(!showAddMenu)}/>
                </div>
            )}
            {isOpen && (
                <TreeList treeType={UpdateKnowledgeTree} treeList={tree.children} dept={dept+1}/>
                )}
        </div>
    );
}

UpdateKnowledgeTree.propTypes = {
    tree: PropTypes.shape({
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
