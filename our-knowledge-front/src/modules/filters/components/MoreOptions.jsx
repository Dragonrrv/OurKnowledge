import {FormattedMessage} from "react-intl";
import {useDispatch, useSelector} from "react-redux";
import users from "../../users";
import React, {useEffect, useState} from "react";
import filters from "../index";
import TechnologyFilter from "./TechnologyFilter";
import {saveFilter} from "../actions";
import * as selectors from "../selectors";
import FilterList from "./FilterList";
import TreeList from "../../common/components/TreeList";
import FilterTechnologyTree from "./FilterTechnologyTree";
import projects from "../../projects";


const MoreOptions = () => {

    const [isOpen, setIsOpen] = useState(false);

    const toggleOpen = () => {
        setIsOpen(!isOpen);
    };

    return (
        <div>
            <div onClick={toggleOpen} style={{display: 'inline-flex', cursor: 'pointer'}}>
                {!isOpen && (
                    <h6 style={{ fontSize: '20px', position: 'relative', transform: 'rotate(180deg)', bottom: '4px'}}>^</h6>
                )}
                {isOpen && (
                    <h6 style={{ fontSize: '20px', position: 'relative', top: '1px'}}>^</h6>
                )}
                <h6><FormattedMessage id='project.global.fields.moreOptions'/></h6>
            </div>
            {isOpen && (
                <div>
                    <TechnologyFilter/>
                </div>
            )}
        </div>
    );

}

export default MoreOptions;