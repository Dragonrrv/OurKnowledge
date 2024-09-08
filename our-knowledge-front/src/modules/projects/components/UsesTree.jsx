
import PropTypes from 'prop-types';

import {useState} from "react";
import TechnologyTreeName from "../../common/components/TechnologyTreeName";
import TreeList from "../../common/components/TreeList";
import TickBox from "../../common/components/TickBox";
import {useDispatch, useSelector} from "react-redux";
import users from "../../users";
import * as actions from "../actions";
import {useIntl} from "react-intl";
import * as selectors from "../selectors";

const UsesTree = ({tree, dept}) => {

    const intl = useIntl();
    const dispatch = useDispatch();
    const userId = useSelector(users.selectors.getUserId);
    const projectDetails = useSelector(selectors.getProjectDetails);
    const [isOpen, setIsOpen] = useState(true);
    const verification = tree.parent.verificationList.find(verification => verification.userId === userId);

    console.log(tree.parent.verificationList)
    console.log(verification)

    const toggleOpen = () => {
        setIsOpen(!isOpen);
    };

    const updateVerification = () => {
        if(verification == null) {
            dispatch(actions.addVerification(userId, tree.parent.usesId))
        } else {
            dispatch(actions.deleteVerification(verification.id, window.confirm(intl.formatMessage({ id: "project.projectDetails.deleteValidation.knowledgeDelete" }))))
        }
    };

    if(tree.parent.usesId==null){
        return null;
    }

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div style={{paddingLeft: 2*dept+'em'}}>
                    <TechnologyTreeName name={tree.parent.name} isOpen={isOpen} hasChildren={tree.children.length>0} onClick={toggleOpen}/>
                </div>
                {projectDetails.participationList.some(participation => participation.user.id === userId) &&
                    <TickBox ok={verification!=null} clickable={true} onClick={updateVerification}/>
                }
            </div>
            {isOpen && (
                <TreeList treeType={UsesTree} treeList={tree.children} dept={dept+1} />
                )}
        </div>
    );
}

UsesTree.propTypes = {
    tree: PropTypes.shape({
        parent: PropTypes.shape({
            usesId: PropTypes.number.isRequired,
            id: PropTypes.number.isRequired,
            name: PropTypes.string.isRequired,
            verificationList: PropTypes.array.isRequired
        }).isRequired,
        children: PropTypes.array.isRequired
    }).isRequired
};

export default UsesTree;
