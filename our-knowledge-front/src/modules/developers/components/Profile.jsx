import {useDispatch, useSelector} from "react-redux";
import {FormattedMessage, useIntl} from "react-intl";

import KnowledgeTreeList from './KnowledgeTreeList';
import * as selectors from '../selectors';
import {useEffect, useState} from "react";
import developers from "../index";
import PropTypes from "prop-types";

const Profile= () => {

    const profile = useSelector(selectors.getProfile);
    const intl = useIntl();
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(developers.actions.findProfile(1, 1));
    }, [dispatch]);

    if (!profile) {
        return <div>Loading...</div>
    }
    console.log(profile)

    return (
        <div>
            <div className="card bg-light border-dark">
                <h5 className="card-header">
                    <FormattedMessage id="project.developers.profile.title"/>
                </h5>
            </div>
            <div className="card bg-light border-dark">
                {profile.user.name}
            </div>
            <div className="card bg-light border-dark">
                {profile.user.email}
            </div>
            <div className="card bg-light border-dark">
                {profile.user.startDate}
            </div>
            <table className="table table-striped table-hover">
                <thead>
                <tr>
                    <th scope="col">
                        <FormattedMessage id='project.global.fields.name'/>
                    </th>
                    <th scope="col" style={{ width: '100px' }}>
                        <FormattedMessage id='project.global.fields.mainSkill'/>
                    </th>
                    <th scope="col" style={{ width: '70px' }}>
                        <FormattedMessage id='project.global.fields.likeIt'/>
                    </th>
                    <th scope="col">
                        <FormattedMessage id='project.developers.profile.'/>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td colSpan="3">
                        <KnowledgeTreeList knowledgeTreeList={profile.knowledgeTreeList} />
                    </td>
                    <td> X </td>
                </tr>
                </tbody>
            </table>
        </div>
    )
}

Profile.protoTypes = {
    user: PropTypes.shape( {
        name: PropTypes.string.isRequired,
        email: PropTypes.string.isRequired,
        startDate: PropTypes.string,
    }).isRequired,

    knowledgeTreeList: PropTypes.array.isRequired
}

export default Profile;