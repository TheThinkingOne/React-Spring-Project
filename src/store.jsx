import { configureStore } from "@reduxjs/toolkit";
import loginSlice from "./slices/loginSlice"; // 확실한 경로 확인 (jsx 확장자 제거)

const store = configureStore({
  reducer: {
    loginSlice: loginSlice, // Prettier가 따옴표를 제거해도 문제없음
  },
});

export default store;

// 로그인 슬라이스에서 리턴하는 거 자체가 리듀서다
