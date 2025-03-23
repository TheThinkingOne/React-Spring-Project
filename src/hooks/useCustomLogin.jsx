// 로그인 후에 보일 정보들 표기하는 훅스
// 2025.02.17 제작

// import { useDispatch, useSelector } from "react-redux";
import { Navigate, useNavigate } from "react-router-dom";
// import { loginPostAsync, logout } from "../slices/loginSlice";
import { useRecoilState, useResetRecoilState } from "recoil";
import { signinState } from "../atoms/signinState";
import { removeCookie, setCookie } from "../util/cookieUtil";
import { loginPost } from "../api/memberApi";
import { cartState } from "../atoms/cartState";

const useCustomLogin = () => {
  //
  const [loginState, setLoginState] = useRecoilState(signinState);

  const resetState = useResetRecoilState(signinState); // 이게 다라고? ㅅㅂ

  const resetCartState = useResetRecoilState(cartState); // 장바구니 지우기

  const navigate = useNavigate();

  const isLogin = loginState.email ? true : false; // 로그인 여부 확인

  // Recoil 사용할거라 useDispatch 사용 비활성화
  // const dispatch = useDispatch();

  // 현재 로그인한 사용자의 정보를 가져다 쓰는 경우가 많으므로

  // Recoil 사용할거라 useSelector 사용 비활성화
  // const loginState = useSelector((state) => state.loginSlice);

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
    //----------로그인 함수
    console.log("doLogin 실행");
    // Recoil 사용함으로 비활성화
    // const action = await dispatch(loginPostAsync(loginParam));
    // return action.payload;
    const result = await loginPost(loginParam);

    saveAsCookie(result);

    return result;
  };

  // 로그아웃 수행 함수
  const doLogout = () => {
    // 로그아웃하면 쿠키정보, 로그인정보, 카트정보 삭제
    removeCookie("member");
    resetState();
    resetCartState();
  };

  const saveAsCookie = (data) => {
    setCookie("member", JSON.stringify(data), 1);
    setLoginState(data);
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
    saveAsCookie, // 이건 소셜 로그인에 쓸 수 있으니 return에 추가
  };
};

export default useCustomLogin;
