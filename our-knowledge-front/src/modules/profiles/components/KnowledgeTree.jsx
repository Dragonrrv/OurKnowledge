
import PropTypes from 'prop-types';

import {useState} from "react";
import TechnologyTreeName from "../../common/components/TechnologyTreeName";
import TickBox from "../../common/components/TickBox";
import TreeList from "../../common/components/TreeList";
import {ProjectLink} from "../../common";

const KnowledgeTree = ({tree, dept}) => {
    const [isOpen, setIsOpen] = useState(true);

    const toggleOpen = () => {
        setIsOpen(!isOpen);
    };

    if(tree.parent.knowledgeId==null){
        return null;
    }

    return (
        <div>
            <div style={{ display: 'flex'}}>
                <div style={{flexBasis: '40%', paddingLeft: 2*dept+'em'}}>
                    <TechnologyTreeName name={tree.parent.name} isOpen={isOpen} hasChildren={tree.children.length>0} onClick={toggleOpen}/>
                </div>
                <div style={{flexBasis: '10%'}}>
                    <TickBox ok={tree.parent.mainSkill} clickable={false}/>
                </div>
                <div style={{flexBasis: '10%'}}>
                    <TickBox ok={tree.parent.likeIt} clickable={false}/>
                </div>

                <div style={{flexBasis: '40%', display: 'flex', gap: '20px', overflow: 'auto'}}>
                    {tree.parent.verificationList.map(verification => (
                        <div key={verification.projectId} style={{ whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis', maxWidth: '100px' }}>
                            <ProjectLink name={verification.projectName} id={verification.projectId} />
                        </div>
                    ))}
                </div>
            </div>
            <div>
                {isOpen && (
                    <TreeList treeType={KnowledgeTree} treeList={tree.children} dept={dept+1} />
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
            mainSkill: PropTypes.bool.isRequired,
            verificationList: PropTypes.array.isRequired
        }).isRequired,
        children: PropTypes.array.isRequired
    }).isRequired

};

export default KnowledgeTree;
