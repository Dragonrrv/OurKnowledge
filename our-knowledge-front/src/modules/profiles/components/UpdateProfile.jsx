import {useDispatch, useSelector} from "react-redux";
import {FormattedMessage} from "react-intl";

import * as selectors from '../selectors';
import {useEffect} from "react";
import profiles from "../index";
import PropTypes from "prop-types";
import users from "../../users";
import TreeList from "../../common/components/TreeList";
import UpdateKnowledgeTree from "./UpdateKnowledgeTree";

const UpdateProfile= () => {

    const profile = useSelector(selectors.getProfile);
    const userId = useSelector(users.selectors.getUserId);
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(profiles.actions.findProfile(userId, userId));
    }, [dispatch]);

    if (!profile) {
        return <div>Loading...</div>
    }

    return (
        <div>
            <div className="card bg-light border-dark">
                <h5 className="card-header">
                    <FormattedMessage id="project.profiles.tittle.updateProfile"/>
                </h5>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div>
                    <FormattedMessage id='project.global.fields.name'/>
                </div>
                <div style={{ marginLeft: 'auto', display: 'flex' }}>
                    <div style={{ width: '70px' }}>
                        <FormattedMessage id='project.global.fields.known'/>
                    </div>
                    <div style={{ width: '100px' }}>
                        <FormattedMessage id='project.global.fields.mainSkill'/>
                    </div>
                    <div style={{ width: 'auto' }}>
                        <FormattedMessage id='project.global.fields.likeIt'/>
                    </div>
                </div>
            </div>
            <div>
                <TreeList treeType={UpdateKnowledgeTree} treeList={profile.knowledgeTreeList} dept={0} />
            </div>
        </div>
    )
}

UpdateProfile.protoTypes = {
    user: PropTypes.shape( {
        name: PropTypes.string.isRequired,
        email: PropTypes.string.isRequired,
        startDate: PropTypes.string,
    }).isRequired,

    knowledgeTreeList: PropTypes.array.isRequired
}

export default UpdateProfile;