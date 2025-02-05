import {useDispatch} from "react-redux";
import {useEffect, useState} from "react";
import users from "../../index";
import UsersResult from "../common/components/UsersResult";
import Users from "./components/Users";
import {Box} from "@mui/material";

const FindUsersDeveloper = () => {

    const dispatch = useDispatch();
    const [keywords, setKeywords] = useState('');

    useEffect(() => {
        dispatch(users.actions.findUsers(1, keywords.trim()));
    }, [dispatch, keywords]);

    return (
        <div>
            <Box sx={{marginBottom: "10px"}}>
                <input id="keywords" type="text" className="form-control mr-sm-2"
                       style={{ maxWidth: '250px'}}
                       value={keywords} onChange={e => setKeywords(e.target.value)}/>
            </Box>
            <UsersResult UsersType={Users} keywords={keywords} useFilter={false}/>
        </div>

    );

}

export default FindUsersDeveloper;