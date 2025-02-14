import { createSlice } from "@reduxjs/toolkit";

const initState = {
  email: "",
};

const loginSlice = createSlice({
  name: "LoginSlice",
  initialState: initState,

  // reducer 함수의 파라미터는 2개까지 밖에 못받는다고 함
  reducers: {
    login: (state, action) => {
      // state : 기존의 상태, action : 파라미터
      console.log("login.....", action);
      console.log(action.payload);
      return { email: action.payload.email }; // 리턴값이 바로 새로운 상태

      // action.payload 가 사용자가 입력하는 실제 이메일 값 => email을 action.payload 의 값으로 사용하겠다 선언
    },
    logout: () => {
      console.log("logout....");
      return { ...initState };
    },
  },
});
export const { login, logout } = loginSlice.actions;
export default loginSlice.reducer;
