import {useDispatch, useSelector} from "react-redux";
import {FormattedMessage} from "react-intl";

import * as selectors from '../selectors';
import React, {useEffect} from "react";
import profiles from "../index";
import PropTypes from "prop-types";
import {useNavigate, useParams} from "react-router-dom";
import users from "../../users";
import {ProjectLink} from "../../common";
import TreeList from "../../common/components/TreeList";
import KnowledgeTree from "./KnowledgeTree";
import filters from "../../filters";

const Profile= () => {
    const { profileId } = useParams();

    const profile = useSelector(selectors.getProfile);
    const userId = useSelector(users.selectors.getUserId);
    const userRole = useSelector(users.selectors.getUserRole);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    useEffect(() => {
        dispatch(profiles.actions.findProfile(profileId));
    }, [dispatch]);

    const useAsFilter = () => {
        dispatch(filters.actions.useUserAsFilter(profile.user.id));
        navigate('/filters/newProjectFilter/' + profile.user.name)
    }

    if (!profile) {
        return <div>Loading...</div>
    }

    return (
        <div>
            <div style={{display: 'flex'}}>
                <div  style={{flexGrow: 1}} className="card text-left">
                    <div className="card-body">
                        <h3>{profile.user.name}</h3>
                        <p>{profile.user.email}</p>
                        <p><span style={{fontWeight:'bold'}}><FormattedMessage id="project.profiles.profile.startDate"/></span>
                            {profile.user.startDate && <div>{profile.user.startDate}</div>}
                            {!profile.user.startDate && <FormattedMessage id="project.global.fields.unknown"/>}</p>
                    </div>
                </div>
                {userId === profile.user.id && (
                    <div style={{marginTop: '20px', marginLeft: '20px', marginRight: '20px'}}>
                        <button className="btn btn-primary my-2 my-sm-0"
                                style={{
                                    padding: '20px 40px', /* Relleno interno */
                                    borderRadius: '5px', /* Borde redondeado */
                                }}
                                onClick={() => navigate('/profiles/updateProfile')}
                        >
                            <FormattedMessage id="project.profiles.button.updateProfile"/>
                        </button>
                    </div>
                )}
                {userRole === "Admin" && (
                    <div style={{ marginLeft: '20px', marginRight: '20px', marginTop: '20px'}}>
                        <button className="btn btn-primary my-2 my-sm-0"
                                style={{
                                    padding: '20px 40px',
                                }}
                                onClick={() => useAsFilter()}
                        >
                            <FormattedMessage id="project.global.buttons.useAsFilter"/>
                        </button>
                    </div>
                )}
            </div>
            <h5><FormattedMessage id="project.profiles.profile.workOn"/></h5>
                {profile.projects.map(project => (
                    <p><ProjectLink name={project.name} id={project.id}/></p>
                ))}
            <div style={{display: 'flex'}}>
                <h6 style={{flexBasis: '40%'}}><FormattedMessage id='project.global.fields.name'/></h6>
                <h6 style={{flexBasis: '10%'}}><FormattedMessage id='project.global.fields.mainSkill'/></h6>
                <h6 style={{flexBasis: '10%'}}><FormattedMessage id='project.global.fields.likeIt'/></h6>
                <h6 style={{flexBasis: '40%'}}><FormattedMessage id='project.profiles.profile.useInProjects'/></h6>
            </div>
            <TreeList treeType={KnowledgeTree} treeList={profile.knowledgeTreeList} dept={0} />
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