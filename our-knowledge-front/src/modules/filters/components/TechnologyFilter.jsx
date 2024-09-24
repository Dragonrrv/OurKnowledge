import {FormattedMessage} from "react-intl";
import TreeList from "../../common/components/TreeList";
import FilterTechnologyTree from "./FilterTechnologyTree";
import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import * as selectors from "../selectors";
import filters from "../index";
import users from "../../users";

const TechnologyFilter = () => {

    const filter = useSelector(selectors.getFilterDetails);
    const userId = useSelector(users.selectors.getUserId);
    const [isOpen, setIsOpen] = useState(false);
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(filters.actions.getDefaultFilter(userId));
    }, [dispatch]);

    const toggleOpen = () => {
        setIsOpen(!isOpen);
    };

    return (
        <div style={{width: '100%', maxWidth: '700px'}}>
            <div onClick={toggleOpen} style={{display: 'inline-flex', cursor: 'pointer'}}>
                {!isOpen && (
                    <h6 style={{ fontSize: '20px', position: 'relative', transform: 'rotate(180deg)', bottom: '4px'}}>^</h6>
                )}
                {isOpen && (
                    <h6 style={{ fontSize: '20px', position: 'relative', top: '1px'}}>^</h6>
                )}
                <h6><FormattedMessage id='project.global.fields.moreOptions'/></h6>
            </div>
            {isOpen && (
                <div>
                    <div style={{display: 'flex'}}>
                        <h6 style={{flexBasis: '60%'}}><FormattedMessage id='project.filters.technologyFilter.title'/></h6>
                        <h6 style={{flexBasis: '20%'}}><FormattedMessage id='project.global.filter.mandatory'/></h6>
                        <h6 style={{flexBasis: '20%'}}><FormattedMessage id='project.global.filter.recommended'/></h6>
                    </div>
                    <div>
                        <TreeList treeType={FilterTechnologyTree} treeList={filter.filterParamTechnologyTreeList} dept={0}/>
                    </div>
                </div>

            )}
        </div>
    );

}

TechnologyFilter.propTypes = {
};

export default TechnologyFilter;