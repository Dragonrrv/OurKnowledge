import {useEffect} from 'react';
import {useDispatch} from 'react-redux';

import Header from './Header';
import Body from './Body';
import Footer from './Footer';
import users from '../../users';

const App = () => {

    const dispatch = useDispatch();

    useEffect(() => {


    }, [dispatch]);

    return (
        <div>
            <Header/>
            <Body/>
            <Footer/>
        </div>
    );

}

export default App;
