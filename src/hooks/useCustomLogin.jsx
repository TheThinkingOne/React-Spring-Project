// 로그인 후에 보일 정보들 표기하는 훅스
// 2025.02.17 제작

import { useDispatch, useSelector } from "react-redux";
import { Navigate, useNavigate } from "react-router-dom";
import { loginPostAsync, logout } from "../slices/loginSlice";

const useCustomLogin = () => {
  const navigate = useNavigate();

  const dispatch = useDispatch();

  // 현재 로그인한 사용자의 정보를 가져다 쓰는 경우가 많으므로

  const loginState = useSelector((state) => state.loginSlice);

  const isLogin = loginState.email ? true : false; // 로그인 여부 확인

  // const doLogin = async (loginParam) => {
  //   try {
  //     const action = await dispatch(loginPostAsync(loginParam)).unwrap(); // unwrap() 사용
  //     return action; // action.payload 대신 바로 action 반환 가능
  //   } catch (error) {
  //     console.error("Login failed:", error);
  //     throw error;
  //   }
  // };

  const doLogin = async (loginParam) => {
    // 로그인 수행 함수
    try {
      const action = await dispatch(loginPostAsync(loginParam)).unwrap(); // unwrap() 사용
      return action; // action.payload 대신 바로 action 반환 가능
    } catch (error) {
      console.error("Login failed:", error);
      throw error;
    }
  };

  // 로그아웃 수행 함수
  const doLogout = () => {
    dispatch(logout());
  };

  // 로그인, 로그아웃 후에 페이지 이동시키는 함수
  const moveToPath = (path) => {
    navigate({ pathname: path }, { replace: true });
  };

  const moveToLogin = () => {
    //----------------------로그인 페이지로 이동
    navigate({ pathname: "/member/login" }, { replace: true });
  };

  const moveToLoginReturn = () => {
    //--------로그인 페이지로 이동 컴포넌트
    return <Navigate replace to="/member/login" />;
  };
  return {
    loginState,
    isLogin,
    doLogin,
    doLogout,
    moveToPath,
    moveToLogin,
    moveToLoginReturn,
  };
};

export default useCustomLogin;
