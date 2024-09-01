
import PropTypes from 'prop-types';

import {useState} from "react";
import TechnologyTreeName from "../../common/components/TechnologyTreeName";
import TreeList from "../../common/components/TreeList";

const UsesTree = ({ tree}) => {
    const [isOpen, setIsOpen] = useState(true);

    const toggleOpen = () => {
        setIsOpen(!isOpen);
    };

    if(tree.parent.usesId==null){
        return null;
    }

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <TechnologyTreeName name={tree.parent.name} isOpen={isOpen} hasChildren={tree.children.length>0} onClick={toggleOpen}/>
            </div>
            <div style={{ paddingLeft: '2em' }}>
                {isOpen && (
                    <TreeList treeType={UsesTree} treeList={tree.children} root={false} />
                    )}
            </div>
        </div>
    );
}

UsesTree.propTypes = {
    tree: PropTypes.shape({
        parent: PropTypes.shape({
            id: PropTypes.number.isRequired,
            name: PropTypes.string.isRequired
        }).isRequired,
        children: PropTypes.array.isRequired
    }).isRequired
};

export default UsesTree;
