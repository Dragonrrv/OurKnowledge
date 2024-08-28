import {useDispatch, useSelector} from "react-redux";
import {FormattedMessage} from "react-intl";

import KnowledgeTreeList from './KnowledgeTreeList';
import * as selectors from '../selectors';
import {useEffect} from "react";
import profiles from "../index";
import PropTypes from "prop-types";
import {useNavigate, useParams} from "react-router-dom";
import users from "../../users";
import {ProjectLink} from "../../common";

const Profile= () => {
    const { profileId } = useParams();

    const profile = useSelector(selectors.getProfile);
    const userId = useSelector(users.selectors.getUserId);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    useEffect(() => {
        dispatch(profiles.actions.findProfile(userId, profileId));
    }, [dispatch]);

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
            </div>
            <h5><FormattedMessage id="project.profiles.profile.workOn"/></h5>
                {profile.projects.map(project => (
                    <ProjectLink name={project.name} id={project.id}/>
                ))}
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
                        <FormattedMessage id='project.profiles.profile.'/>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td colSpan="3">
                        <KnowledgeTreeList knowledgeTreeList={profile.knowledgeTreeList} root={true} />
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