import TechnologyTreeName from "../../common/components/TechnologyTreeName";
import TreeList from "../../common/components/TreeList";
import PropTypes from "prop-types";
import {useEffect, useState} from "react";
import * as actions from "../actions";
import {useDispatch, useSelector} from "react-redux";
import * as selectors from "../selectors";
import {Checkbox} from "@mui/material";

const FilterTechnologyTree = ({tree, dept}) => {

    const dispatch = useDispatch();
    const filterId = useSelector(selectors.getFilterId);
    const [isOpen, setIsOpen] = useState(true);
    const [needRender, setNeedRender] = useState(false)

    useEffect(() => {
        reRender()
    },[tree.parent.mandatory, tree.parent.recommended]);

    const reRender = () => {
        setNeedRender(true)
        const timer = setTimeout(() => setNeedRender(false), 0);
        return () => clearTimeout(timer);
    }

    const toggleOpen = () => {
        setIsOpen(!isOpen);
    };

    const updateMandatory = () => {
        dispatch(actions.updateFilterParam(tree.parent.filterParamId, filterId, tree.parent.id,
            !tree.parent.mandatory, false))
    };

    const updateRecommended = () => {
        dispatch(actions.updateFilterParam(tree.parent.filterParamId, filterId, tree.parent.id,
            false, !tree.parent.recommended))
    };

    return (
        <div>
            <div style={{display: 'flex'}}>
                <div style={{flexBasis: '60%', paddingLeft: 2*dept+'em'}}>
                    <TechnologyTreeName name={tree.parent.name} isOpen={isOpen} hasChildren={tree.children.length>0} onClick={toggleOpen}/>
                </div>
                {!needRender && !tree.parent.unnecessary && (
                    <div style={{flexBasis: '40%', display: 'flex', maxHeight: '1px', marginTop: '-6px'}}>
                        <div style={{flexBasis: '50%'}}>
                            <Checkbox checked={tree.parent.mandatory} color="success" onClick={updateMandatory} />
                        </div>
                        {!tree.parent.recommendedUnnecessary && (
                            <div style={{flexBasis: '50%'}}>
                                <Checkbox checked={tree.parent.recommended} color="success" onClick={updateRecommended} />
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
    }).isRequired,
    dept: PropTypes.number.isRequired
};

export default FilterTechnologyTree;