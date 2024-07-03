import PropTypes from "prop-types";

const TickBox = ({ok, clickable, onClick}) => {

    return (
        <button onClick={onClick} style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            width: '20px',
            height: '20px',
            border: '2px solid #4CAF50',
            borderRadius: '8px',
            backgroundColor: '#e8f5e9',
            position: 'relative',
            cursor: clickable ? 'pointer' : '',
        }}>
            {ok && (
                <span style={{
                    fontSize: '16px',
                    cursor: clickable ? 'pointer' : '',
                    color: '#4CAF50'
                }}>
                &#10004;
            </span>
            )}
        </button>
    )
}

TickBox.propTypes = {
    onClick: PropTypes.func
};

export default TickBox;