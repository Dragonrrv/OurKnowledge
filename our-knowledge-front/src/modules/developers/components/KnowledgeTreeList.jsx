import PropTypes from 'prop-types';

import KnowledgeTree from "./KnowledgeTree";

const KnowledgeTreeList = ({ knowledgeTreeList, root}) => {

    const rootStyle = {
        background: 'linear-gradient(to bottom, #F5F3F3 50%, #EDECEC 50%)',
        backgroundSize: '100% 60px'
    };

    const defaultStyle = {};

    return (
        <div style={root ? rootStyle : defaultStyle}>
            {knowledgeTreeList.map(knowledgeTree => (
                <div key={knowledgeTree.id} onContextMenu={e => e.stopPropagation()}>
                    <KnowledgeTree knowledgeTree={knowledgeTree}/>
                </div>
            ))}
        </div>

    )
}

KnowledgeTreeList.propTypes = {
    knowledgeTreeList: PropTypes.array.isRequired
}

export default KnowledgeTreeList;