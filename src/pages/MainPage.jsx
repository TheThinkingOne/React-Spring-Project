import React from 'react';
import { Link } from 'react-router-dom';
import BasicLayout from '../layouts/BasicLayout.jsx';

function MainPage(props) {
    return (
        /* <div className={'text-3xl'}>

            <div className={'flex'}>
                <Link to = {'/about'}>Go to About Page</Link>
            </div>

            <div>MainPage</div>
        </div> */
        <BasicLayout>
            <div className={'text-3xl'}>Main Page</div>
        </BasicLayout>
    );
}

export default MainPage;

