import React, { useCallback } from 'react';
import BasicLayout from '../../layouts/BasicLayout';
import { Outlet, useNavigate } from 'react-router-dom';

function IndexPage(props) {

    const navigate = useNavigate()

    const handleClickList = useCallback(() => {
        navigate({pathname:'list'}) // navigate 호출하여 경로 변경
    },[])

    const handleClickAdd = useCallback(() => {
        navigate({pathname:'add'}) // pathname을 통해 이동 경로 지정
    },[])

    return (
        <BasicLayout>
            <div className="w-full flex m-2 p-2 ">
                <div className="text-xl m-1 p-2 w-20 font-extrabold text-center underline"
                    onClick={handleClickList}
                >LIST
                </div>
                <div className="text-xl m-1 p-2 w-20 font-extrabold text-center underline"
                    onClick={handleClickAdd}
                >ADD
                </div>
            </div>
        <div className="flex flex-wrap w-full">
        <Outlet/>
        </div>
        </BasicLayout>
    );
}

export default IndexPage;