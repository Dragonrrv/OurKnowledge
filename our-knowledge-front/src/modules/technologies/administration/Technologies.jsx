import {useDispatch, useSelector} from "react-redux";
import {FormattedMessage} from "react-intl";

import * as selectors from '../selectors';
import {useEffect} from "react";
import technologies from "../index";
import TreeList from "../../common/components/TreeList";
import TechnologyTree from "./components/TechnologyTree";
import AddTechnology from "../../common/components/AddTechnology";
import {Box} from "@mui/material";

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
            <Box>
                <Box sx={{display: "flex", marginTop: "20px"}}>
                    <Box sx={{flexGrow: "1"}}>
                        <h6><FormattedMessage id='project.global.fields.name'/></h6>
                        <TreeList treeType={TechnologyTree} treeList={technologyTreeList} dept={0}/>
                        <AddTechnology parentId={null} relevant={true}/>
                    </Box>
                    <Box sx={{flexGrow: "1"}}>
                        <h6><FormattedMessage id='project.technologies.technologies.statistics'/></h6>
                    </Box>
                </Box>
            </Box>
        </div>
    )
}

export default Technologies;