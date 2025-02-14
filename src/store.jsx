// src/store.jsx
import { configureStore } from "@reduxjs/toolkit";
import loginSlice from "./slices/loginSlice";

// 🚀 Redux Store 설정
const store = configureStore({
  reducer: {
    loginSlice, // loginSlice 등록
  },
  devTools: process.env.NODE_ENV !== "production", // Redux DevTools 연결
});

export default store;
