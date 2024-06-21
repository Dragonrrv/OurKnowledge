import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {useState} from "react";
import {Errors} from "../../common";
import {FormattedMessage} from "react-intl";
import users from "../../users";


const List = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();
    const userRole = useSelector(users.selectors.getUserRole);
    const [backendErrors, setBackendErrors] = useState(null);


    return (
        <div>
            <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
            <div className="card bg-light border-dark">
                <h5 className="card-header">
                    <FormattedMessage id="project.technologies.List.title"/>
                </h5>
                <div className="card-body">
                    <label htmlFor="parentTechnology" className="col-md-3 col-form-label">

                    </label>
                </div>
            </div>
        </div>
    )
}