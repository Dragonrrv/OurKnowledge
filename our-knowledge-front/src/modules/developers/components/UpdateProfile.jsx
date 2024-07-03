import {useDispatch, useSelector} from "react-redux";
import {FormattedMessage, useIntl} from "react-intl";

import KnowledgeTreeList from './KnowledgeTreeList';
import * as selectors from '../selectors';
import {useEffect, useState} from "react";
import developers from "../index";
import PropTypes from "prop-types";
import UpdateKnowledgeTreeList from "./UpdateKnowledgeTreeList";

const UpdateProfile= () => {

    const profile = useSelector(selectors.getProfile);
    const intl = useIntl();
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(developers.actions.findProfile(1, 1));
    }, [dispatch]);

    if (!profile) {
        return <div>Loading...</div>
    }

    return (
        <div>
            <div className="card bg-light border-dark">
                <h5 className="card-header">
                    <FormattedMessage id="project.developers.tittle.updateProfile"/>
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
                <UpdateKnowledgeTreeList knowledgeTreeList={profile.knowledgeTreeList} root={true} />
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