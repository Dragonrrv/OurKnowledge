
import PropTypes from 'prop-types';

import {useEffect, useState, useRef } from "react";
import * as actions from "../../actions";
import {useDispatch} from "react-redux";
import {useIntl} from "react-intl";
import AddTechnology from "../../../common/components/AddTechnology";
import TechnologyTreeName from "../../../common/components/TechnologyTreeName";
import TreeList from "../../../common/components/TreeList";
import {Button, IconButton} from "@mui/material";
import {Delete} from "@mui/icons-material";

const TechnologyTree = ({tree, dept}) => {

    const intl = useIntl();
    const dispatch = useDispatch();
    const [isOpen, setIsOpen] = useState(true);
    const [showAddMenu, setShowAddMenu] = useState(false);
    const [addMenuPosition, setAddMenuPosition] = useState({ x: 0, y: 0 });
    const contextMenuRef = useRef(null);

    const toggleOpen = () => {
        setIsOpen(!isOpen);
    };

    const handleRemove = () => {
        const hasChildren = tree.children.length > 0;
        const confirmMessage = hasChildren
            ? 'project.technologies.technology.haveChildren.explanation'
            : 'project.technologies.technology.delete.confirmation';

        if (window.confirm(intl.formatMessage({ id: confirmMessage }))) {
            dispatch(actions.removeTechnology(tree.parent.id, hasChildren));
        }
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
    }, [showAddMenu]);

    return (
        <div>
            <div onContextMenu={handleAddMenu} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div style={{paddingLeft: 2*dept+'em'}}>
                    <TechnologyTreeName name={tree.parent.name} isOpen={isOpen} hasChildren={tree.children.length>0} onClick={toggleOpen}/>
                </div>
                <div style={{height: "20px", marginTop: "-15px"}}>
                    <IconButton title={`${intl.formatMessage({ id: 'project.global.buttons.delete' })} ${tree.parent.name}`}
                                onClick={handleRemove}>
                        <Delete scale="0.8" />
                    </IconButton>
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
                                   relevant={true}
                                   onAdd = {() =>setShowAddMenu(!showAddMenu)}/>
                </div>
            )}
            {isOpen && (
                <TreeList treeType={TechnologyTree} treeList={tree.children} dept={dept+1}/>
            )}
        </div>
    );
}

TechnologyTree.propTypes = {
    tree: PropTypes.shape({
        parent: PropTypes.shape({
            id: PropTypes.number.isRequired,
            name: PropTypes.string.isRequired
        }).isRequired,
        children: PropTypes.array.isRequired
    }).isRequired,
    dept: PropTypes.number.isRequired
};

export default TechnologyTree;
