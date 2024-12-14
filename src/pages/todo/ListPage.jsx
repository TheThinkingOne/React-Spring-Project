import React from 'react';
import BasicLayout from '../../layouts/BasicLayout';
import { useSearchParams } from 'react-router-dom';

function ListPage(props) {

    // 페이지 리스트 작성
    const [queryParams] = useSearchParams()
    
    // URL의 쿼리 스트링에서 page와 size를 읽어오고, 
    // 기본값(없으면 page=1, size=10)을 설정.
    const page = queryParams.get('page') ? parseInt(queryParams.get('page')) : 1
    const size = queryParams.get('size') ? parseInt(queryParams.get('size')) : 10


    return (
        <div className="p-4 w-full bg-white">
            <div className="text-3xl font-extrabold">
                Todo List Page Component --- {page} --- {size}
            </div>
        </div>
    );
}

export default ListPage;