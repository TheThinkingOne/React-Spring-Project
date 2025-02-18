import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { loginPost } from "../api/memberApi";
import { getCookie, removeCookie, setCookie } from "../util/cookieUtil";

const initState = {
  email: "",
};

// 2025/02/18
const loadMemberCookie = () => {
  // 쿠키에서 로그인 정보 가져오기
  const memberInfo = getCookie("member"); // 쿠키에서 사용자 정보 가져오기

  return memberInfo;
};

// 비동기 로그인 요청 생성
export const loginPostAsync = createAsyncThunk(
  "loginPostAsync",
  (param) => loginPost(param) // 비동기 로그인 요청
);

const loginSlice = createSlice({
  name: "LoginSlice",
  initialState: loadMemberCookie() || initState, // 쿠키에 있는 이메일 혹은 빈 이메일(로그아웃상태)

  // reducer 함수의 파라미터는 2개까지 밖에 못받는다고 함
  reducers: {
    login: (state, action) => {
      // state : 기존의 상태, action : 파라미터
      console.log("login.....", action);
      console.log(action.payload);
      return { email: action.payload.email }; // 리턴값이 바로 새로운 상태
      // 이메일을 action.payload.email로 설정

      // action.payload 가 사용자가 입력하는 실제 이메일 값 => email을 action.payload 의 값으로 사용하겠다 선언
    },
    logout: () => {
      console.log("logout....");

      removeCookie("member");

      return { ...initState }; // 초기상태(비로그인 상태)로 리셋하여 로그아웃 처리
    },
  },

  extraReducers: (builder) => {
    builder
      .addCase(loginPostAsync.fulfilled, (state, action) => {
        console.log("fulfilled"); // 성공

        const payload = action.payload;

        // 페이로드 , 쿠키 관련 설정 추가
        if (!payload.error) {
          setCookie("member", JSON.stringify(payload), 1);
        }

        return payload;
      })
      .addCase(loginPostAsync.pending, (state, action) => {
        console.log("pending"); // 요청중
      })
      .addCase(loginPostAsync.rejected, (state, action) => {
        console.log("rejected"); // 실패
      });
  },
  //
});
export const { login, logout } = loginSlice.actions;
export default loginSlice.reducer;
