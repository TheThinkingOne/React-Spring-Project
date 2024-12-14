import { Navigate } from "react-router-dom";
import { Children, Suspense, lazy } from "react";

const Loading = <div>Loading....</div>;

const TodoList = lazy(() => import("../pages/todo/ListPage.jsx"));

const TodoRead = lazy(() => import("../pages/todo/ReadPage.jsx"));

const TodoAdd = lazy(() => import("../pages/todo/AddPage.jsx"));

const TodoModify = lazy(() => import("../pages/todo/ModifyPage.jsx"));


const todoRouter = () => {
    // todo 리스트 배열 반환해주는 함수
    // todo 와 관련된 설정은 여기서 해결

    // 각각의 경로는 동적 import(lazy)와 Suspense를 사용하여 코드 스플리팅 적용
    return [
        {
            path : 'list', // ListPage
            element: <Suspense fallback={Loading}><TodoList/></Suspense>
        },
        {
            // 리다이렉션(네비게이트)
            path: '',
            element: <Navigate replace={true} to={'list'}/>
            // replace가 리다이렉트 역할하는것인가
        },
        {
            path: 'read/:tno', // ReadPage
            element: <Suspense fallback={Loading}><TodoRead/></Suspense>
        },
        {
            path: 'add', // AddPage
            element: <Suspense fallback={Loading}><TodoAdd/></Suspense>
        },
        {
            path: 'modify/:tno', // ModifyPage
            element: <Suspense fallback={Loading}><TodoModify/></Suspense>
        }
    ]

}

export default todoRouter;