import { createBrowserRouter } from "react-router-dom";
import { Suspense, lazy } from "react";
import todoRouter from "./todoRouter.jsx";
import productRouters from "./ProductsRouter.jsx";
import memberRouter from "./memberRouter.jsx";

const Loading = <div>Loading....</div>;

const Main = lazy(() => import("../pages/MainPage.jsx"));
const About = lazy(() => import("../pages/AboutPage.jsx"));
const TodoIndex = lazy(() => import("../pages/todo/IndexPage.jsx"));
const ProductsIndex = lazy(() => import("../pages/products/IndexPage.jsx"));

const root = createBrowserRouter([
  {
    path: "",
    element: (
      <Suspense fallback={Loading}>
        <Main />
      </Suspense>
    ),
  },
  {
    path: "about",
    element: (
      <Suspense fallback={Loading}>
        <About />
      </Suspense>
    ),
  },
  {
    path: "todo",
    element: (
      <Suspense fallback={Loading}>
        <TodoIndex />
      </Suspense>
    ),
    children: todoRouter(), // 'children'으로 수정
  },
  {
    path: "products",
    element: (
      <Suspense fallback={Loading}>
        <ProductsIndex />
      </Suspense>
    ),
    children: productRouters(),
  },
  {
    path: "member",
    children: memberRouter(),
  },
]);

export default root;
