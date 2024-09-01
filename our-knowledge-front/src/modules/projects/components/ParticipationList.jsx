import {FormattedMessage} from "react-intl";
import PropTypes from "prop-types";
import ProfileLink from "../../common/components/ProfileLink";


const ParticipationList = ({participationList}) => {

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
                    <tr key={participation.id}>
                        <td>
                            <ProfileLink id={participation.user.id} name={participation.user.email}/>
                        </td>
                        <td>
                            {participation.startDate}
                        </td>
                        <td>
                            {participation.endDate}
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
    )

}

ParticipationList.protoTypes = {
    participationList: PropTypes.array.isRequired
}

export default ParticipationList;