import {useDispatch, useSelector} from "react-redux";
import projects from "../index";
import {useState} from "react";
import * as actions from "../actions";
import TechnologyTreeName from "../../common/components/TechnologyTreeName";
import TickBox from "../../common/components/TickBox";
import TreeList from "../../common/components/TreeList";

const UpdateUsesTree = ({tree, dept}) => {

    const dispatch = useDispatch();

    const projectId = useSelector(projects.selectors.getProjectId);
    console.log(projectId)
    const [isOpen, setIsOpen] = useState(true);

    const toggleOpen = () => {
        setIsOpen(!isOpen);
    };

    const changeUses = (used, technologyId) => {
        if(used){
            dispatch(actions.deleteUses(tree.parent.usesId));
        } else {
            dispatch(actions.addUses(projectId, technologyId));
        }
    }

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div style={{paddingLeft: 2*dept+'em'}}>
                    <TechnologyTreeName name={tree.parent.name} isOpen={isOpen} hasChildren={tree.children.length>0} onClick={toggleOpen}/>
                </div>
                <TickBox ok={tree.parent.usesId!=null} clickable={true}
                         onClick={() => changeUses(tree.parent.usesId!=null, tree.parent.id)}/>

            </div>
            {isOpen && (
                <TreeList treeType={UpdateUsesTree} treeList={tree.children} dept={dept+1} />
            )}
        </div>
    )
}

export default UpdateUsesTree;