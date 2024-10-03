import {FormattedMessage} from "react-intl";
import TreeList from "../../common/components/TreeList";
import FilterTechnologyTree from "./FilterTechnologyTree";
import {useSelector} from "react-redux";
import * as selectors from "../selectors";

const TechnologyFilter = () => {

    const filter = useSelector(selectors.getFilterDetails);

    return (
        <div style={{width: '100%', maxWidth: '700px'}}>
            <div style={{display: 'flex'}}>
                <h6 style={{flexBasis: '60%'}}><FormattedMessage id='project.filters.technologyFilter.title'/></h6>
                <h6 style={{flexBasis: '20%'}}><FormattedMessage id='project.global.filter.mandatory'/></h6>
                <h6 style={{flexBasis: '20%'}}><FormattedMessage id='project.global.filter.recommended'/></h6>
            </div>
            <div>
                <TreeList treeType={FilterTechnologyTree} treeList={filter.filterParamTechnologyTreeList} dept={0}/>
            </div>
        </div>
    );

}

TechnologyFilter.propTypes = {
};

export default TechnologyFilter;