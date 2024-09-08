import PropTypes from 'prop-types';

import TechnologyTree from "./TechnologyTree";
import AddTechnology from "../../common/components/AddTechnology";

const TechnologyTreeList = ({ technologyTreeList, root}) => {

    const rootStyle = {
        background: 'linear-gradient(to bottom, #F5F3F3 50%, #EDECEC 50%)',
        backgroundSize: '100% 60px'
    };

    const defaultStyle = {};

    return (
        <div style={root ? rootStyle : defaultStyle}>
            {technologyTreeList.map(technologyTree => (
                <div key={technologyTree.id} onContextMenu={e => e.stopPropagation()}>
                    <TechnologyTree tree={technologyTree}/>
                </div>
            ))}
            {root &&
                <AddTechnology parentId={null}
                               userId={2}
                               relevant={true} onAdd={null}/>}
        </div>
    );
}

TechnologyTreeList.propTypes = {
    technologyTreeList: PropTypes.array.isRequired
}

export default TechnologyTreeList;