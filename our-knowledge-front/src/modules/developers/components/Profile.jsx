import {useDispatch, useSelector} from "react-redux";
import {FormattedMessage} from "react-intl";

import KnowledgeTreeList from './KnowledgeTreeList';
import * as selectors from '../selectors';
import {useEffect} from "react";
import developers from "../index";
import PropTypes from "prop-types";
import {useNavigate} from "react-router-dom";

const Profile= () => {

    const profile = useSelector(selectors.getProfile);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    useEffect(() => {
        dispatch(developers.actions.findProfile(1, 1));
    }, [dispatch]);

    if (!profile) {
        return <div>Loading...</div>
    }

    return (
        <div>
            <div style={{display: 'flex'}}>
                <div style={{flexGrow: 1}}>
                    <div className="card bg-light border-dark">
                        <h5 className="card-header">
                            {profile.user.name}
                        </h5>
                    </div>
                    <div className="card bg-light border-dark">
                        {profile.user.email}
                    </div>
                    <div className="card bg-light border-dark">
                        <FormattedMessage id="project.developers.profile.startDate"/>
                        {profile.user.startDate && <div>{profile.user.startDate}</div>}
                        {!profile.user.startDate && <FormattedMessage id="project.global.fields.unknown"/>}
                    </div>
                </div>
                <div style={{marginTop: '20px', marginLeft: '20px', marginRight: '20px'}}>
                    <button
                        style={{
                            backgroundColor: 'blue', /* Tono gris */
                            color: 'white', /* Color del texto */
                            padding: '20px 40px', /* Relleno interno */
                            border: 'none', /* Sin borde */
                            borderRadius: '5px', /* Borde redondeado */
                            cursor: 'pointer', /* Cursor de mano */
                            fontSize: '16px', /* Tamaño de fuente */
                            fontWeight: 'bold', /* Negrita */
                            transition: 'background-color 0.3s ease, transform 0.3s ease' /* Transiciones suaves */
                        }}
                        onClick={() => navigate('/developers/updateProfile')}
                    >
                        <FormattedMessage id="project.developers.button.updateProfile"/>
                    </button>
                </div>
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