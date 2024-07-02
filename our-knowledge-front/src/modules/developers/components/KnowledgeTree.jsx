
import PropTypes from 'prop-types';

import KnowledgeTreeList from "./KnowledgeTreeList";
import {useEffect, useState, useRef } from "react";
import * as actions from "../actions";
import {useDispatch} from "react-redux";
import {FormattedMessage, useIntl} from "react-intl";
import AddTechnology from "../../common/components/AddTechnology";

const KnowledgeTree = ({ knowledgeTree }) => {
    const [isOpen, setIsOpen] = useState(true);

    const toggleOpen = () => {
        setIsOpen(!isOpen);
    };

    function box(ok) {
        if(ok){
            return <OkBox/>
        }
        return <EmptyBox/>
    }

    const OkBox = (
        <div className="ok-box">
                                <span style = {{ display: 'flex', justifyContent: 'center', alignItems: 'center',
                                    width: '100px',height: '100px', border: '2px solid #4CAF50', borderRadius: '8px',
                                    backgroundColor: '#e8f5e9', position: 'relative'}}>
                                    &#10004;</span>
        </div>
    )

    const EmptyBox = (
        <div className="ok-box">
                                <span style = {{ display: 'flex', justifyContent: 'center', alignItems: 'center',
                                    width: '100px',height: '100px', border: '2px solid #4CAF50', borderRadius: '8px',
                                    backgroundColor: '#e8f5e9', position: 'relative'}}>
                                    X</span>
        </div>
    )

    const Box = ({ ok }) => (
        <div style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            width: '20px',
            height: '20px',
            border: '2px solid #4CAF50',
            borderRadius: '8px',
            backgroundColor: '#e8f5e9',
            position: 'relative'
        }}>
            {ok && (
                <span style={{
                    fontSize: '16px',
                    color: '#4CAF50'
                }}>
                &#10004;
            </span>
            )}
        </div>
    );

    if(knowledgeTree.parent.knowledgeId==null){
        return null;
    }

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div onClick={toggleOpen} style={{ cursor: 'pointer' }}>
                    {!isOpen && knowledgeTree.children.length>0 && (
                        <span style={{ color: 'darkgrey', fontSize: '20px', display: 'inline-block', transform: 'rotate(180deg)' }}>^</span>
                    )}
                    {isOpen && knowledgeTree.children.length>0 && (
                        <span style={{ color: 'darkgrey', fontSize: '20px', position: 'relative', top: '5px' }}>^</span>
                    )}
                    {knowledgeTree.children.length<1 && (<span style={{fontSize: '20px'}}> </span>)}
                    {knowledgeTree.parent.name}
                </div>
                <div style={{ display: 'flex', gap: '60px' }}>
                    <Box ok={knowledgeTree.parent.mainSkill} />
                    <Box ok={knowledgeTree.parent.likeIt} />
                </div>
            </div>
            <div style={{ paddingLeft: '2em' }}>
                {isOpen && (
                    <KnowledgeTreeList
                        knowledgeTreeList={knowledgeTree.children}
                        root={false}
                    />
                )}
            </div>
        </div>
    );
}

KnowledgeTree.propTypes = {
    knowledgeTree: PropTypes.shape({
        parent: PropTypes.shape({
            id: PropTypes.number.isRequired,
            name: PropTypes.string.isRequired,
            relevant:  PropTypes.bool.isRequired,
            knowledgeId: PropTypes.number.isRequired,
            likeIt: PropTypes.bool.isRequired,
            mainSkill: PropTypes.bool.isRequired
        }).isRequired,
        children: PropTypes.array.isRequired
    }).isRequired

};

export default KnowledgeTree;
