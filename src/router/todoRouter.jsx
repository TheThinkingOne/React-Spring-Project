import { Navigate } from "react-router-dom";
import { Children, Suspense, lazy } from "react";

const Loading = <div>Loading....</div>;

const TodoList = lazy(() => import("../pages/todo/ListPage.jsx"));

const TodoRead = lazy(() => import("../pages/todo/ReadPage.jsx"));


const todoRouter = () => {
    // todo 리스트 배열 반환해주는 함수
    // todo 와 관련된 설정은 여기서 해결
    return [
        {
            path : 'list',
            element: <Suspense fallback={Loading}><TodoList/></Suspense>
        },
        {
            // 리다이렉션(네비게이트)
            path: '',
            element: <Navigate replace={true} to={'list'}/>
            // replace가 리다이렉트 역할하는것인가
        },
        {
            path: 'read/:tno', // 이게 아이디 같은것
            element: <Suspense fallback={Loading}><TodoRead/></Suspense>
        }
    ]

}

export default todoRouter;