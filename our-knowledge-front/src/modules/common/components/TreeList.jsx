import PropTypes from "prop-types";
import {Box} from "@mui/material";

const TreeList = ({ treeType: TreeTypeComponent, treeList, dept }) => {

    const rootStyle = {
        border: '2px #F5F3F3 solid',
        borderRadius: '4px',
        background: 'linear-gradient(to bottom, #F5F3F3 50%, #EDECEC 50%)',
        backgroundSize: '100% 60px'
    };

    const defaultStyle = {};

    return (
        <Box sx={dept===0 ? rootStyle : defaultStyle}>
            {treeList.map(tree => (
                <div key={tree.id} onContextMenu={e => e.stopPropagation()}>
                    <TreeTypeComponent tree={tree} dept={dept} />
                </div>
            ))}
        </Box>
    );
};

TreeList.propTypes = {
    treeType: PropTypes.elementType.isRequired,
    treeList: PropTypes.array.isRequired,
    root: PropTypes.bool,
    dept: PropTypes.number
};

export default TreeList;