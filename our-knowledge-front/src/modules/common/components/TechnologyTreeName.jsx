import PropTypes from "prop-types";

const TechnologyTreeName = ({name, isOpen, hasChildren, onClick}) => {

    return (
        <div onClick={onClick} style={{ cursor: 'pointer'}}>
            {!isOpen && hasChildren && (
                <span style={{ color: 'darkgrey', fontSize: '20px', display: 'inline-block', transform: 'rotate(180deg)' }}>^</span>
            )}
            {isOpen && hasChildren && (
                <span style={{ color: 'darkgrey', fontSize: '20px', position: 'relative', top: '5px' }}>^</span>
            )}
            {!hasChildren && (<span style={{fontSize: '20px'}}> </span>)}
            {name}
        </div>
    )
}

TechnologyTreeName.propTypes = {
    onClick: PropTypes.func.isRequired
};

export default TechnologyTreeName;