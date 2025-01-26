import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import projects from "../index";
import technologies from "../../technologies";
import {FormattedMessage} from "react-intl";
import ProjectForm from "../components/ProjectForm";
import TreeList from "../../common/components/TreeList";
import UpdateUsesTree from "../add/components/UpdateUsesTree";
import {Box, Button, Checkbox, Divider} from "@mui/material";

const UpdateProject = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();
    const projectDetails = useSelector(projects.selectors.getProjectDetails);
    const [useProcessing, setUseProcessing] = useState(true);
    const [useAI, setUseAI] = useState(false);
    const [selectedFile, setSelectedFile] = useState(null);

    const handleFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
    };

    const updateProject = (name, description, startDate, status, size) => {
        dispatch(projects.actions.updateProject(projectDetails.project.id, name, description, startDate, status, size, false, []));
        navigate('/projects/projectDetails/0')
    }

    const updateUseProcessing = () => {
        setUseProcessing(!useProcessing);
    };

    const updateUseAI = () => {
        setUseAI(!useAI);
    };

    const reader = new FileReader();

    reader.onload = function(e) {
        dispatch(projects.actions.sendFile(projectDetails.project.id, selectedFile.name.split('.').pop(), e.target.result, useProcessing, useAI));
    };

    const sendFile = (event) => {
        event.preventDefault();
        if (!selectedFile) {
            alert("Por favor selecciona un archivo");
            return;
        }
        reader.readAsText(selectedFile);
    };

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
            <Divider style={{marginTop: '10px'}}/>
            <Box container spacing={2} sx={{border: '1px solid black', borderRadius: '8px', padding: '16px'}}>
                <input style={{marginTop: '10px'}} type={"file"} onChange={handleFileChange}/>
                <div style={{display: "flex", marginTop: '10px'}}>
                    <div>
                        <h6><FormattedMessage id='project.projects.updateProject.useProcessing'/></h6>
                        <div style={{maxHeight: '15px', marginTop: '-15px'}}>
                            <Checkbox checked={useProcessing} color="success" onClick={updateUseProcessing}/>
                        </div>
                    </div>
                    <div style={{marginLeft: '20px'}}>
                        <h6 style={{}}><FormattedMessage id='project.projects.updateProject.useAI'/></h6>
                        <div style={{maxHeight: '15px', marginTop: '-15px'}}>
                            <Checkbox checked={useAI} color="success" onClick={updateUseAI}/>
                        </div>
                    </div>
                    <Button style={{marginLeft: '25px'}} variant="contained" color="primary"
                            onClick={sendFile}
                    >
                        <FormattedMessage id='project.projects.updateProject.button.sendFile'/>
                    </Button>
                </div>
                <Divider style={{marginTop: '20px'}}/>
                <h6 style={{marginTop: '10px'}}><FormattedMessage id='project.projects.addProject.TechnologiesUsed'/>
                </h6>
                <div style={{width: '350px'}}>
                    <TreeList treeType={UpdateUsesTree} treeList={projectDetails.usesTreeList} dept={0}/>
                </div>
            </Box>
        </div>
    );

}

export default UpdateProject;