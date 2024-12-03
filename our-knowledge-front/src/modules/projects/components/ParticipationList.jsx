import {FormattedMessage} from "react-intl";
import ProfileLink from "../../common/components/ProfileLink";
import React, {useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import users from "../../users";
import * as actions from "../actions";


const ParticipationList = ({participationList, projectId}) => {

    const dispatch = useDispatch();
    const userRole = useSelector(users.selectors.getUserRole);
    const userId = useSelector(users.selectors.getUserId);

    const [updateStartDate, setUpdateStartDate] = useState(null);
    const [startDate, setStartDate] = useState(null);
    const [updateEndDate, setUpdateEndDate] = useState(null);
    const [endDate, setEndDate] = useState(null);

    const updateParticipation = (participationId) => {
        dispatch(actions.updateParticipation(participationId, updateStartDate, updateEndDate))
    };

    const addParticipation = () => {
        dispatch(actions.addParticipation(projectId, startDate, endDate))
    };

    return (
        <table className="table table-striped table-hover">
            <thead><tr>
                <th scope="col">
                    <FormattedMessage id='project.global.fields.email'/>
                </th>
                <th scope="col">
                    <FormattedMessage id='project.global.fields.startDate'/>
                </th>
                <th scope="col">
                    <FormattedMessage id='project.global.fields.endDate'/>
                </th>
            </tr></thead>
            <tbody>
            {participationList.map(participation => (
                <React.Fragment key={participation.id}>
                    <tr>
                        <td>
                            <ProfileLink id={participation.user.id} name={participation.user.email} />
                        </td>
                        <td>
                            {participation.startDate}
                        </td>
                        <td>
                            {participation.endDate}
                        </td>
                    </tr>

                    {participation.user.id === userId && (
                        <tr>
                            <td>
                                <button className="btn btn-primary my-2 my-sm-0"
                                        onClick={() => updateParticipation(participation.id)}>
                                    <FormattedMessage id="project.projects.button.updateParticipation" />
                                </button>
                            </td>
                            <td>
                                <input id="updateStartDate" type="date" className="form-control mr-sm-2" style={{ width: '150px' }}
                                       value={updateStartDate} onChange={e => setUpdateStartDate(e.target.value)} />
                            </td>
                            <td>
                                <input id="updateEndDate" type="date" className="form-control mr-sm-2" style={{ width: '150px' }}
                                       value={updateEndDate} onChange={e => setUpdateEndDate(e.target.value)} />
                            </td>
                        </tr>
                    )}
                </React.Fragment>
            ))}

            {userRole === "Developer" && (
                <tr>
                    <td>
                        <button className="btn btn-primary my-2 my-sm-0"
                                onClick={addParticipation}>
                            <FormattedMessage id="project.projects.button.addParticipation" />
                        </button>
                    </td>
                    <td>
                        <input id="startDate" type="date" className="form-control mr-sm-2" style={{ width: '150px' }}
                               value={startDate} onChange={e => setStartDate(e.target.value)} />
                    </td>
                    <td>
                        <input id="endDate" type="date" className="form-control mr-sm-2" style={{ width: '150px' }}
                               value={endDate} onChange={e => setEndDate(e.target.value)} />
                    </td>
                </tr>
            )}

            {participationList.some(participation => participation.user.id === userId) && (
                <tr>
                    <td colSpan="3">
                        <div style={{ backgroundColor: "white", color: "green" }}>
                            *<FormattedMessage id="project.projects.participationList.alreadyParticipate" />
                        </div>
                    </td>
                </tr>
            )}
            </tbody>
        </table>
    )

}

export default ParticipationList;