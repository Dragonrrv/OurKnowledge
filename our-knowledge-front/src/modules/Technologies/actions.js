import * as actionTypes from './actionTypes';
import backend from '../../backend';


const listCompleted = technologiesTree => ({
    type: actionTypes.LIST_COMPLETED,
    technologiesTree
});

export const list = (userName, password, onSuccess, onErrors) => dispatch =>
    backend.technologyService.listRelevantTechnologies(
        technologiesTree => {
            dispatch(listCompleted(technologiesTree));
            onSuccess();
        },
        onErrors,
    );
