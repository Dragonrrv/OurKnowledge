import TechnologyTreeName from "../../common/components/TechnologyTreeName";
import TickBox from "../../common/components/TickBox";
import TreeList from "../../common/components/TreeList";
import PropTypes from "prop-types";
import {useState} from "react";
import * as actions from "../actions";
import {useDispatch, useSelector} from "react-redux";
import users from "../../users";
import * as selectors from "../selectors";

const FilterTechnologyTree = ({tree, dept}) => {

    const dispatch = useDispatch();
    const userId = useSelector(users.selectors.getUserId);
    const filterId = useSelector(selectors.getFilterId);
    const [isOpen, setIsOpen] = useState(true);

    const toggleOpen = () => {
        setIsOpen(!isOpen);
    };

    const updateMandatory = () => {
        dispatch(actions.updateFilterParam(userId, tree.parent.filterParamId, filterId, tree.parent.id,
            !tree.parent.mandatory, false))
    };

    const updateRecommended = () => {
        dispatch(actions.updateFilterParam(userId, tree.parent.filterParamId, filterId, tree.parent.id,
            false, !tree.parent.recommended))
    };

    return (
        <div>
            <div style={{display: 'flex'}}>
                <div style={{flexBasis: '60%', paddingLeft: 2*dept+'em'}}>
                    <TechnologyTreeName name={tree.parent.name} isOpen={isOpen} hasChildren={tree.children.length>0} onClick={toggleOpen}/>
                </div>
                {!tree.parent.unnecessary && (
                    <div style={{flexBasis: '40%', display: 'flex', marginTop: '5px'}}>
                        <div style={{flexBasis: '50%'}}>
                            <TickBox ok={tree.parent.mandatory} clickable={true} onClick={updateMandatory}/>
                        </div>
                        {!tree.parent.recommendedUnnecessary && (
                            <div style={{flexBasis: '50%'}}>
                                <TickBox ok={tree.parent.recommended} clickable={true} onClick={updateRecommended}/>
                            </div>
                        )}
                    </div>
                )}
            </div>
            {isOpen && (
                <TreeList treeType={FilterTechnologyTree} treeList={tree.children} dept={dept+1} />
            )}
        </div>
    );
}

FilterTechnologyTree.propTypes = {
    tree: PropTypes.shape({
        parent: PropTypes.shape({
            id: PropTypes.number.isRequired,
            name: PropTypes.string.isRequired,
            filterParamId: PropTypes.number,
            mandatory: PropTypes.bool,
            recommended: PropTypes.bool,
            unnecessary: PropTypes.bool.isRequired,
            recommendedUnnecessary: PropTypes.bool.isRequired
        }).isRequired,
        children: PropTypes.array.isRequired
    }).isRequired
};

export default FilterTechnologyTree;