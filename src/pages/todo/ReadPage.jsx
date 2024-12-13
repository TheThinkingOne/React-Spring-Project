import React from 'react';
import { useParams } from 'react-router-dom';

function ReadPage(props) {

    const tno = useParams() // 리액트의 훅스 기능
    console.log(tno)

    return (
        <div className={'text-3xl'}>
            Todo Read Page
        </div>
    );
}

export default ReadPage;