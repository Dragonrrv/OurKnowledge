import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import projects from "../index";
import technologies from "../../technologies";
import {FormattedMessage} from "react-intl";
import ProjectForm from "./ProjectForm";
import TreeList from "../../common/components/TreeList";
import UpdateUsesTree from "./UpdateUsesTree";

const UpdateProject = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();
    const projectDetails = useSelector(projects.selectors.getProjectDetails);

    console.log(projectDetails);
    const updateProject = (name, description, startDate, status, size) => {
        dispatch(projects.actions.updateProject(projectDetails.project.id, name, description, startDate, status, size, false, []));
        navigate('/projects/projectDetails/0')
    }

    useEffect(() => {
        dispatch(technologies.actions.findTechnologies());
    }, [dispatch]);

    return (
        <div>
            <ProjectForm nameV={projectDetails.project.name} descriptionV={projectDetails.project.description}
                         startDateV={projectDetails.project.startDate} statusV={projectDetails.project.status}
                         sizeV={projectDetails.project.size}
                         buttonV={<FormattedMessage id='project.global.buttons.update'/>}
                         onClick={updateProject}/>

            <h6 style={{marginTop: '10px'}}><FormattedMessage id='project.projects.addProject.TechnologiesUsed'/></h6>
            <div style={{ width: '300px'}}>
                <TreeList treeType={UpdateUsesTree} treeList={projectDetails.usesTreeList} dept={0} />
            </div>
        </div>
    );

}

export default UpdateProject;