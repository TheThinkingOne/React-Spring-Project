import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";
import { RouterProvider } from "react-router-dom";
import root from "./router/root";
import "./index.css"; // TailwindCSS 파일 import
import BasicMenu from "./components/menus/BasicMenu";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";

// 리액트 쿼리 관련 설정 코드 추가

const queryClient = new QueryClient();

function App() {
  return (
    <>
      {/* <BasicMenu /> */}
      <QueryClientProvider client={queryClient}>
        <RouterProvider router={root} />
        <ReactQueryDevtools initialIsOpen={true}></ReactQueryDevtools>
        {/*개발자 도구 관련 설정 (아래에 뜸)*/}
      </QueryClientProvider>
    </>
  );
}

export default App;
