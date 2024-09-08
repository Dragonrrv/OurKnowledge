import {useDispatch, useSelector} from "react-redux";
import {FormattedMessage} from "react-intl";

import * as selectors from '../selectors';
import {useEffect} from "react";
import technologies from "../index";
import TreeList from "../../common/components/TreeList";
import TechnologyTree from "./TechnologyTree";

const Technologies= () => {

    const technologyTreeList = useSelector(selectors.getTechnologies);
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(technologies.actions.findTechnologies());
    }, [dispatch]);

    if (!technologyTreeList) {
        return <div>Loading...</div>
    }

    return (
        <div>
            <div className="card bg-light border-dark">
                <h5 className="card-header">
                    <FormattedMessage id="project.technologies.technologies.title"/>
                </h5>
            </div>
            <table className="table table-striped table-hover">
                <thead>
                <tr>
                    <th scope="col">
                        <FormattedMessage id='project.global.fields.name'/>
                    </th>
                    <th scope="col">
                        <FormattedMessage id='project.technologies.technologies.statistics'/>
                    </th>
                </tr>
                </thead>
                <tbody>
                    <tr>
                        <TreeList treeType={TechnologyTree} treeList={technologyTreeList} dept={0}/>
                    </tr>
                </tbody>
            </table>
        </div>
    )
}

export default Technologies;