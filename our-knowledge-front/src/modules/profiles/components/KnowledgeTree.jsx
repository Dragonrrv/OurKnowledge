
import PropTypes from 'prop-types';

import {useState} from "react";
import TechnologyTreeName from "../../common/components/TechnologyTreeName";
import TickBox from "../../common/components/TickBox";
import TreeList from "../../common/components/TreeList";

const KnowledgeTree = ({ tree }) => {
    const [isOpen, setIsOpen] = useState(true);

    const toggleOpen = () => {
        setIsOpen(!isOpen);
    };

    if(tree.parent.knowledgeId==null){
        return null;
    }

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <TechnologyTreeName name={tree.parent.name} isOpen={isOpen} hasChildren={tree.children.length>0} onClick={toggleOpen}/>
                <div style={{ display: 'flex', gap: '60px' }}>
                    <TickBox ok={tree.parent.mainSkill} clickable={false} />
                    <TickBox ok={tree.parent.likeIt} clickable={false} />
                </div>
            </div>
            <div style={{ paddingLeft: '2em' }}>
                {isOpen && (
                    <TreeList treeType={KnowledgeTree} treeList={tree.children} root={false} />
                    )}
            </div>
        </div>
    );
}

KnowledgeTree.propTypes = {
    tree: PropTypes.shape({
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
