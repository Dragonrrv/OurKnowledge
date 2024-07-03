
import PropTypes from 'prop-types';

import TechnologyTreeList from "./TechnologyTreeList";
import {useEffect, useState, useRef } from "react";
import * as actions from "../actions";
import {useDispatch} from "react-redux";
import {useIntl} from "react-intl";
import AddTechnology from "../../common/components/AddTechnology";
import TechnologyTreeName from "../../common/components/TechnologyTreeName";

const TechnologyTree = ({ technologyTree}) => {
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
        const hasChildren = technologyTree.children.length > 0;
        const confirmMessage = hasChildren
            ? 'project.administration.technology.haveChildren.explanation'
            : 'project.administration.technology.delete.confirmation';

        if (window.confirm(intl.formatMessage({ id: confirmMessage }))) {
            dispatch(actions.removeTechnology(2, technologyTree.parent.id, hasChildren));
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
    }, []);

    return (
        <div>
            <div onContextMenu={handleAddMenu} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <TechnologyTreeName name={technologyTree.parent.name} isOpen={isOpen} hasChildren={technologyTree.children.length>0} onClick={toggleOpen}/>
                <button
                    onClick={handleRemove}
                    title={`${intl.formatMessage({ id: 'project.global.buttons.delete' })} ${technologyTree.parent.name}`}
                    style={{
                        color: 'white',
                        marginLeft: '10px',
                        border: 'none',
                        background: 'red',
                        cursor: 'pointer',
                        borderRadius: '30%',
                        height: '25px', // Ajusta la altura
                        lineHeight: '25px', // Ajusta la altura del texto
                    }}
                >
                    X
                </button>
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
                    <AddTechnology parentId={technologyTree.parent.id}
                    onAdd = {() =>setShowAddMenu(!showAddMenu)}/>
                </div>
            )}
            <div style={{ paddingLeft: '2em' }}>
                {isOpen && (
                    <TechnologyTreeList
                        technologyTreeList={technologyTree.children}
                        root={false}
                    />
                )}
            </div>
        </div>
    );
}

TechnologyTree.propTypes = {
    technologyTree: PropTypes.shape({
        parent: PropTypes.shape({
            id: PropTypes.number.isRequired,
            name: PropTypes.string.isRequired
        }).isRequired,
        children: PropTypes.array.isRequired
    }).isRequired
};

export default TechnologyTree;
