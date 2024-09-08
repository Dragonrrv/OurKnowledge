import PropTypes from "prop-types";

const TreeList = ({ treeType: TreeTypeComponent, treeList, dept }) => {

    const rootStyle = {
        background: 'linear-gradient(to bottom, #F5F3F3 50%, #EDECEC 50%)',
        backgroundSize: '100% 60px',
    };

    const defaultStyle = {};

    return (
        <div style={dept===0 ? rootStyle : defaultStyle}>
            {treeList.map(tree => (
                <div key={tree.id} onContextMenu={e => e.stopPropagation()}>
                    <TreeTypeComponent tree={tree} dept={dept} />
                </div>
            ))}
        </div>
    );
};

TreeList.propTypes = {
    treeType: PropTypes.elementType.isRequired,
    treeList: PropTypes.array.isRequired,
    root: PropTypes.bool,
};

export default TreeList;