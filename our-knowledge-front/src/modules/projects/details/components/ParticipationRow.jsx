import {FormattedMessage} from "react-intl";
import ProfileLink from "../../../common/components/ProfileLink";
import {useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import users from "../../../users";
import * as actions from "../../actions";
import PropTypes from "prop-types";
import {
    Button, ButtonGroup,
    TableCell,
    TableRow
} from "@mui/material";


const ParticipationRow = ({participation}) => {

    const dispatch = useDispatch();
    const userId = useSelector(users.selectors.getUserId);
    const [updateMode, setUpdateMode] = useState(false);
    const [updateStartDate, setUpdateStartDate] = useState(participation.startDate);
    const [updateEndDate, setUpdateEndDate] = useState(participation.endDate);

    const updateParticipation = () => {
        dispatch(actions.updateParticipation(participation.id, updateStartDate, updateEndDate))
        setUpdateMode(false);
    };

    const cancelUpdate = () => {
        setUpdateMode(false);
        setUpdateStartDate(participation.startDate);
        setUpdateEndDate(participation.endDate);
    }

    const profileCell = (
        <TableCell>
            <ProfileLink id={participation.user.id} name={participation.user.email} />
        </TableCell>
    );

    return (
        updateMode ? (
            <TableRow key={participation.user.id}>
                {profileCell}
                <TableCell>
                    <input id="startDate" type="date" className="form-control mr-sm-2" style={{ width: '150px' }}
                           value={updateStartDate} onChange={e => setUpdateStartDate(e.target.value)} />
                </TableCell>
                <TableCell>
                    <input id="endDate" type="date" className="form-control mr-sm-2" style={{ width: '150px' }}
                           value={updateEndDate} onChange={e => setUpdateEndDate(e.target.value)} />
                </TableCell>
                {participation.user.id === userId && (
                    <TableCell align="right">
                        <ButtonGroup variant="contained" aria-label="Basic button group">
                            <Button onClick={updateParticipation}>
                                <FormattedMessage id="project.global.buttons.save" />
                            </Button>
                            <Button onClick={cancelUpdate}>
                                <FormattedMessage id="project.global.buttons.cancel" />
                            </Button>
                        </ButtonGroup>
                    </TableCell>
                )}
            </TableRow>
        ) : (
            <TableRow key={participation.user.id}>
                {profileCell}
                <TableCell>
                    {participation.startDate}
                </TableCell>
                <TableCell>
                    {participation.endDate || <FormattedMessage id="project.global.fields.undefined" />}
                </TableCell>
                {participation.user.id === userId && (
                    <TableCell align="right">
                        <Button variant="contained" onClick={setUpdateMode}>
                            <FormattedMessage id="project.global.buttons.update" />
                        </Button>
                    </TableCell>
                )}
            </TableRow>
        )
    )

}

ParticipationRow.propTypes = {
    participation: PropTypes.shape({
        id: PropTypes.number.isRequired,
        user: PropTypes.shape({
            id: PropTypes.number.isRequired,
            email: PropTypes.string.isRequired
        }).isRequired,
        startDate: PropTypes.string.isRequired,
        endDate: PropTypes.string.isRequired
    }).isRequired
};

export default ParticipationRow;