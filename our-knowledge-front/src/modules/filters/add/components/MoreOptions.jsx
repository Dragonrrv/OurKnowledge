import {FormattedMessage} from "react-intl";
import {useState} from "react";
import TechnologyFilter from "../../components/TechnologyFilter";
import {ExpandLess, ExpandMore} from "@mui/icons-material";


const MoreOptions = () => {

    const [isOpen, setIsOpen] = useState(false);

    const toggleOpen = () => {
        setIsOpen(!isOpen);
    };

    return (
        <div>
            <div onClick={toggleOpen} style={{display: 'inline-flex', cursor: 'pointer'}}>
                <span style={{marginLeft: "-2px", marginTop: "-5px"}}>
                    {isOpen ? (
                        <ExpandLess/>
                    ) : (
                        <ExpandMore/>
                    )}
                </span>
                <h6><FormattedMessage id='project.global.fields.moreOptions'/></h6>
            </div>
            {isOpen && (
                <div>
                    <TechnologyFilter/>
                </div>
            )}
        </div>
    );

}

export default MoreOptions;