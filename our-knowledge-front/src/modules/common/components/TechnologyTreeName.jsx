import PropTypes from "prop-types";
import {ExpandLess, ExpandMore} from "@mui/icons-material";

const TechnologyTreeName = ({name, isOpen, hasChildren, onClick}) => {

    return (
        <div onClick={onClick} style={{...(hasChildren && {cursor: 'pointer'}), fontSize: '20px'}}>
            {hasChildren ? (
                <span style={{marginLeft: "-2px"}}>
                    {isOpen ? (
                        <ExpandLess/>
                    ) : (
                        <ExpandMore/>
                    )}
                </span>
                )
                : (<span style={{marginLeft: "8px"}}/>)}
            <span style={{fontSize: '16px', marginLeft: "-4px"}}>{name}</span>
        </div>
    )
}

TechnologyTreeName.propTypes = {
    name: PropTypes.string.isRequired,
    isOpen: PropTypes.bool.isRequired,
    hasChildren: PropTypes.bool.isRequired,
    onClick: PropTypes.func.isRequired
};

export default TechnologyTreeName;