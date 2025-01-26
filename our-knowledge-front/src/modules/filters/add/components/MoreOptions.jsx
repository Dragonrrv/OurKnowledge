import {FormattedMessage} from "react-intl";
import {useState} from "react";
import TechnologyFilter from "../../components/TechnologyFilter";


const MoreOptions = () => {

    const [isOpen, setIsOpen] = useState(false);

    const toggleOpen = () => {
        setIsOpen(!isOpen);
    };

    return (
        <div>
            <div onClick={toggleOpen} style={{display: 'inline-flex', cursor: 'pointer'}}>
                {!isOpen && (
                    <h6 style={{ fontSize: '20px', position: 'relative', transform: 'rotate(180deg)', bottom: '4px'}}>^</h6>
                )}
                {isOpen && (
                    <h6 style={{ fontSize: '20px', position: 'relative', top: '1px'}}>^</h6>
                )}
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