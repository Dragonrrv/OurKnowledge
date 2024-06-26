import * as actionTypes from './actionTypes';
import backend from '../../backend';
import * as selectors from "./selectors";

const technologiesUpdated = technologies => ({
    type: actionTypes.TECHNOLOGIES_UPDATED,
    technologies
})

const findTechnologiesCompleted = technologies => ({
    type: actionTypes.FIND_TECHNOLOGIES_COMPLETED,
    technologies
});

export const findTechnologies = () => dispatch => {

    dispatch(clearTechnologyTree());
    backend.technologyService.listRelevantTechnologies(
        result => dispatch(findTechnologiesCompleted(result)))
}

const clearTechnologyTree = () => ({
    type: actionTypes.CLEAR_TECHNOLOGY_TREE
});

export const addTechnology = (userId, technologyName, parentId, onSuccess,
                                       onErrors) => dispatch =>
    backend.technologyService.addTechnology(userId,
        technologyName, parentId, technology => {
            dispatch(technologiesUpdated(technology));
            onSuccess();
        },
        onErrors);

export const removeTechnology = (userId, technologyId, deleteChildren, onSuccess,
                                       onErrors) => dispatch =>
    backend.technologyService.deleteTechnology(userId,
        technologyId, deleteChildren, technology => {
            dispatch(technologiesUpdated(technology));
            onSuccess();
        },
        onErrors);
