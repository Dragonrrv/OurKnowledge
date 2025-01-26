import {FormattedMessage} from "react-intl";
import {useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import users from "../../../users";
import * as actions from "../../actions";
import PropTypes from "prop-types";
import {Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import ParticipationRow from "./ParticipationRow";


const ParticipationList = ({participationList, projectId}) => {

    const dispatch = useDispatch();
    const userRole = useSelector(users.selectors.getUserRole);
    const userId = useSelector(users.selectors.getUserId);

    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);

    const addParticipation = () => {
        dispatch(actions.addParticipation(projectId, startDate, endDate))
    };

    return (
        <TableContainer component={Paper}>
            <Table aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell><FormattedMessage id='project.global.fields.email'/></TableCell>
                        <TableCell><FormattedMessage id='project.global.fields.startDate'/></TableCell>
                        <TableCell><FormattedMessage id='project.global.fields.endDate'/></TableCell>
                        <TableCell></TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {participationList.map(participation => (
                        <ParticipationRow participation={participation} key={participation.user.id} />
                    ))}
                    {userRole === "Developer" && !participationList.some(participation => participation.user.id === userId) && (
                        <TableRow>
                            <TableCell>
                                <Button variant="contained" onClick={addParticipation}>
                                    <FormattedMessage id="project.projects.button.addParticipation" />
                                </Button>
                            </TableCell>
                            <TableCell>
                                <input id="startDate" type="date" className="form-control mr-sm-2" style={{ width: '150px' }}
                                       value={startDate} onChange={e => setStartDate(e.target.value)} />
                            </TableCell>
                            <TableCell>
                                <input id="endDate" type="date" className="form-control mr-sm-2" style={{ width: '150px' }}
                                       value={endDate} onChange={e => setEndDate(e.target.value)} />
                            </TableCell>
                            <TableCell></TableCell>
                        </TableRow>
                    )}
                </TableBody>
            </Table>
        </TableContainer>
    )

}

ParticipationList.propTypes = {
    participationList: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.number.isRequired,
            user: PropTypes.shape({
                id: PropTypes.number.isRequired,
                email: PropTypes.string.isRequired
            }).isRequired,
            startDate: PropTypes.string.isRequired,
            endDate: PropTypes.string.isRequired
        })
    ).isRequired,
    projectId: PropTypes.number.isRequired
};

export default ParticipationList;