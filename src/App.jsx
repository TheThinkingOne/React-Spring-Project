import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";
import { RouterProvider } from "react-router-dom";
import root from "./router/root";
import "./index.css"; // TailwindCSS 파일 import
import BasicMenu from "./components/menus/BasicMenu";

function App() {
  return (
    <>
      <BasicMenu />
      <RouterProvider router={root} />
    </>
  );
}

export default App;
