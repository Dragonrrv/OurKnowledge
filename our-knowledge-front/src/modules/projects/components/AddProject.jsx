import { FormattedMessage } from "react-intl";
import { useEffect } from "react";
import projects from "../index";
import technologies from "../../technologies";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import ProjectForm from "./ProjectForm";

const AddProject = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const addProject = (name, description, startDate, status, size) => {
        dispatch(projects.actions.addProject(name, description, startDate, status, size, []));
        navigate('/projects/projectDetails/0');
    };

    useEffect(() => {
        dispatch(technologies.actions.findTechnologies());
    }, [dispatch]);

    return (
        <ProjectForm
            buttonV={<FormattedMessage id='project.global.buttons.create'/>}
            onClick={addProject}/>
    );
};

export default AddProject;