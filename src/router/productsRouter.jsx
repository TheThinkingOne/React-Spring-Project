import { Suspense, lazy } from "react";
import { Navigate } from "react-router-dom";
import ModifyPage from "../pages/products/ModifyPage.jsx";

const Loading = <div>Loading....</div>;

// Path 경로들
const ProductList = lazy(() => import("../pages/products/ListPage.jsx"));

const ProductAdd = lazy(() => import("../pages/products/AddPage.jsx"));

const ProductRead = lazy(() => import("../pages/products/ReadPage.jsx"));

const ProductModify = lazy(() => import("../pages/products/ModifyPage.jsx"));

const productRouters = () => {
  return [
    {
      path: "List",
      element: (
        <Suspense fallback={Loading}>
          <ProductList />
        </Suspense>
      ),
    },
    {
      // 아무것도 경로가 없다면
      path: "",
      element: <Navigate replace to={"/products/list"}></Navigate>,
    },
    {
      path: "Add",
      element: (
        <Suspense fallback={Loading}>
          <ProductAdd />
        </Suspense>
      ),
    },
    {
      path: "read/:pno",
      element: (
        <Suspense fallback={Loading}>
          <ProductRead />
        </Suspense>
      ),
    },
    {
      path: "modify/:pno",
      element: (
        <Suspense fallback={Loading}>
          <ModifyPage />
        </Suspense>
      ),
    },
  ];
};

export default productRouters;
