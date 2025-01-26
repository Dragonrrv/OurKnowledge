import {useDispatch, useSelector} from "react-redux";
import projects from "../../index";
import {useEffect, useState} from "react";
import * as actions from "../../actions";
import TechnologyTreeName from "../../../common/components/TechnologyTreeName";
import TreeList from "../../../common/components/TreeList";
import PropTypes from "prop-types";
import {Checkbox} from "@mui/material";

const UpdateUsesTree = ({tree, dept}) => {

    const dispatch = useDispatch();
    const projectId = useSelector(projects.selectors.getProjectId);
    const [isOpen, setIsOpen] = useState(true);
    const [isChecked, setIsChecked] = useState(tree.parent.usesId != null);

    useEffect(() => {
        setIsChecked(tree.parent.usesId != null);
    }, [tree.parent.usesId]);

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
                    <TechnologyTreeName name={tree.parent.name} isOpen={isOpen} hasChildren={tree.children.length>0}
                                        onClick={toggleOpen}/>
                </div>
                <div style={{maxHeight: '1px', marginTop: '-40px' }}>
                    <Checkbox checked={isChecked} color="success"
                              onClick={() => changeUses(isChecked, tree.parent.id)}/>
                </div>
            </div>
            {isOpen && (
                <TreeList treeType={UpdateUsesTree} treeList={tree.children} dept={dept+1} />
            )}
        </div>
    )
}


UpdateUsesTree.propTypes = {
    tree: PropTypes.shape({
        parent: PropTypes.shape({
            name: PropTypes.string.isRequired,
            id: PropTypes.number.isRequired,
            usesId: PropTypes.number
        }).isRequired,
        children: PropTypes.arrayOf(PropTypes.object).isRequired
    }).isRequired,
    dept: PropTypes.number.isRequired,
};

export default UpdateUsesTree;