import {useDispatch, useSelector} from "react-redux";
import {FormattedMessage, useIntl} from "react-intl";

import TechnologyTreeList from './TechnologyTreeList';
import * as selectors from '../selectors';
import * as actions from '../actions';
import {useEffect, useState} from "react";
import administration from "../index";
import TechnologyTree from "./TechnologyTree";

const Technologies= () => {

    const technologyTreeList = useSelector(selectors.getTechnologies);
    const intl = useIntl();
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(administration.actions.findTechnologies());
    }, [dispatch]);

    if (!technologyTreeList) {
        return <div>Loading...</div>
    }

    console.log(technologyTreeList)
    return (
        <div>
            <div className="card bg-light border-dark">
                <h5 className="card-header">
                    <FormattedMessage id="project.administration.technologies.title"/>
                </h5>
            </div>
            <table className="table table-striped table-hover">
                <thead>
                <tr>
                    <th scope="col">
                        <FormattedMessage id='project.global.fields.name'/>
                    </th>
                    <th scope="col">
                        <FormattedMessage id='project.administration.technologies.statistics'/>
                    </th>
                </tr>
                </thead>
                <tbody>
                    <tr>
                        <TechnologyTreeList technologyTreeList={technologyTreeList} parentTechnologyId={null}/>
                    </tr>
                </tbody>
            </table>
        </div>
    )
}

export default Technologies;