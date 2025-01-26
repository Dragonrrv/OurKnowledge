import { useDispatch, useSelector } from "react-redux";
import { FormattedMessage } from "react-intl";
import * as selectors from '../selectors';
import { useEffect } from "react";
import profiles from "../index";
import PropTypes from "prop-types";
import {useNavigate, useParams} from "react-router-dom";
import users from "../../users";
import TreeList from "../../common/components/TreeList";
import KnowledgeTree from "./components/KnowledgeTree";
import filters from "../../filters";
import {Box, Button, Chip, Divider, Grid2} from "@mui/material";

const Profile = () => {
    const { profileId } = useParams();

    const profile = useSelector(selectors.getProfile);
    const userId = useSelector(users.selectors.getUserId);
    const userRole = useSelector(users.selectors.getUserRole);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    useEffect(() => {
        dispatch(profiles.actions.findProfile(profileId));
    }, [dispatch, profileId]);

    const useAsFilter = () => {
        dispatch(filters.actions.useUserAsFilter(profile.user.id));
        navigate('/filters/newProjectFilter/' + profile.user.name);
    };

    if (!profile) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <div style={{ display: 'flex' }}>
                <Box sx={{flexGrow: 1, border: '1px solid black', borderRadius: '8px', padding: '16px'}}>
                    <h3>{profile.user.name}</h3>
                    <p>{profile.user.email}</p>
                    <p>
                        <span style={{ fontWeight: 'bold' }}>
                            <FormattedMessage id="project.profiles.profile.startDate" />
                            {": "}
                        </span>
                        {profile.user.startDate ? <span>{profile.user.startDate}</span> :
                        <FormattedMessage id="project.global.fields.unknown" />}
                    </p>
                </Box>
                {userId === profile.user.id && (
                    <div style={{ marginTop: '20px', marginLeft: '20px', marginRight: '20px' }}>
                        <Button variant="contained" color="primary" href="/profiles/updateProfile">
                            <FormattedMessage id="project.profiles.button.updateProfile" />
                        </Button>
                    </div>
                )}
                {userRole === "Admin" && (
                    <div style={{ marginLeft: '20px', marginRight: '20px', marginTop: '20px' }}>
                        <Button variant="contained" color="primary"
                            onClick={useAsFilter}
                        >
                            <FormattedMessage id="project.global.buttons.useAsFilter" />
                        </Button>
                    </div>
                )}
            </div>
            <Box sx={{border: '1px solid black', borderRadius: '8px', padding: '16px', marginTop: '20px'}}>
                <h5>
                    <FormattedMessage id="project.profiles.profile.projects" />{": "}
                </h5>
                <Grid2 container spacing={3}>
                    {profile.projects.map((project) => (
                        <Grid2 key={project.id}>
                            <Chip label={project.name} component="a" href={`/projects/projectDetails/${project.id}`} clickable />
                        </Grid2>
                    ))}
                </Grid2>
            </Box>
            <Divider sx={{ marginTop: '20px', marginBottom: '20px' }} />
            <div style={{ display: 'flex' }}>
                <h6 style={{ flexBasis: '40%' }}>
                    <FormattedMessage id="project.global.fields.knowledge" />
                </h6>
                <h6 style={{ flexBasis: '10%' }}>
                    <FormattedMessage id="project.global.fields.mainSkills" />
                </h6>
                <h6 style={{ flexBasis: '10%' }}>
                    <FormattedMessage id="project.global.fields.likedSkills" />
                </h6>
                <h6 style={{ flexBasis: '40%' }}>
                    <FormattedMessage id="project.profiles.profile.useInProjects" />
                </h6>
            </div>
            <TreeList treeType={KnowledgeTree} treeList={profile.knowledgeTreeList} dept={0} />
        </div>
    );
};

Profile.protoTypes = {
    user: PropTypes.shape( {
        name: PropTypes.string.isRequired,
        email: PropTypes.string.isRequired,
        startDate: PropTypes.string,
    }).isRequired,

    knowledgeTreeList: PropTypes.array.isRequired
}

export default Profile;
