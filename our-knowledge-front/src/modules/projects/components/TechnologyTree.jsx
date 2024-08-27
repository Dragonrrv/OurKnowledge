
import PropTypes from 'prop-types';

import TechnologyTreeList from "./TechnologyTreeList";
import {useEffect, useState, useRef } from "react";
import * as actions from "../actions";
import {useDispatch, useSelector} from "react-redux";
import {useIntl} from "react-intl";
import AddTechnology from "../../common/components/AddTechnology";
import TechnologyTreeName from "../../common/components/TechnologyTreeName";
import users from "../../users";

const TechnologyTree = ({ technologyTree}) => {
    const [isOpen, setIsOpen] = useState(true);

    const toggleOpen = () => {
        setIsOpen(!isOpen);
    };

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <TechnologyTreeName name={technologyTree.parent.name} isOpen={isOpen} hasChildren={technologyTree.children.length>0} onClick={toggleOpen}/>
            </div>
            <div style={{ paddingLeft: '2em' }}>
                {isOpen && (
                    <TechnologyTreeList
                        technologyTreeList={technologyTree.children}
                        root={false}
                    />
                )}
            </div>
        </div>
    );
}

TechnologyTree.propTypes = {
    technologyTree: PropTypes.shape({
        parent: PropTypes.shape({
            id: PropTypes.number.isRequired,
            name: PropTypes.string.isRequired
        }).isRequired,
        children: PropTypes.array.isRequired
    }).isRequired
};

export default TechnologyTree;
