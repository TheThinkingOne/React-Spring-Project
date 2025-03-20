// src/main.jsx
import React from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import store from "./store.jsx";
import { Provider } from "react-redux";
import { RouterProvider } from "react-router-dom"; // ✅ RouterProvider 추가
import root from "./router/root.jsx"; // ✅ router 경로 확인
import App from "./App.jsx";
import { RecoilRoot } from "recoil";

// 🚀 Redux + Router 적용
// createRoot(document.getElementById("root")).render(
//   <Provider store={store}>
//     <RouterProvider router={root} />
//   </Provider>
// );

//
createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <Provider store={store}>
      <RecoilRoot>
        <App />
      </RecoilRoot>
    </Provider>
  </React.StrictMode>
);
