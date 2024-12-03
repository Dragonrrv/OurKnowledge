import {useDispatch, useSelector} from "react-redux";
import {FormattedMessage} from "react-intl";

import * as selectors from '../selectors';
import React, {useEffect} from "react";
import profiles from "../index";
import PropTypes from "prop-types";
import users from "../../users";
import TreeList from "../../common/components/TreeList";
import UpdateKnowledgeTree from "./UpdateKnowledgeTree";
import AddTechnology from "../../common/components/AddTechnology";

const UpdateProfile= () => {

    const dispatch = useDispatch();
    const profile = useSelector(selectors.getProfile);
    const userId = useSelector(users.selectors.getUserId);


    useEffect(() => {
        dispatch(profiles.actions.findProfile(userId));
    }, [dispatch]);

    const updateUser = (startDate) => {
        dispatch(users.actions.updateUser(startDate));
    }

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
            <div style={{ display: 'flex', alignItems: 'center', marginTop: '10px'}}>
                <h6><FormattedMessage id='project.profiles.profile.startDate' /></h6>
                <input id="startDate" type="date" className="form-control mr-sm-2" style={{ width: '150px', marginLeft: '10px'}}
                       value={profile.startDate} onChange={e => updateUser(e.target.value)} />
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '10px'}}>
                <h6><FormattedMessage id='project.profiles.updateProfile.knowledge' /></h6>
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
                <AddTechnology parentId={null} relevant={false}/>
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