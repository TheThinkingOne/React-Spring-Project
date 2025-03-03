import { Suspense, lazy } from "react";

const Loading = <div>Loading....</div>;

// 로그인과 로그아웃
const Login = lazy(() => import("../pages/member/LoginPage"));
const Logout = lazy(() => import("../pages/member/LogoutPage"));

const KakaoRedirect = lazy(() => import("../pages/member/KaKaoRedirectPage"));

// 로그인 시 비밀번호 변경하라고 설정하는 페이지
const MemberModify = lazy(() => import("../pages/member/ModifyPage"));

const memberRouter = () => {
  return [
    {
      path: "login",
      element: (
        <Suspense fallback={Loading}>
          <Login />
        </Suspense>
      ),
    },
    {
      path: "logout",
      element: (
        <Suspense fallback={Loading}>
          <Logout />
        </Suspense>
      ),
    },
    // 2025/02/19 카카오 리다이랙트 추가
    {
      path: "kakao",
      // 여기에 오타가 있었음. element 가 아니고 elment 로!! 씨발!
      element: (
        <Suspense fallback={Loading}>
          <KakaoRedirect />
        </Suspense>
      ),
    },
    {
      path: "modify",
      element: (
        <Suspense fallback={Loading}>
          <MemberModify />
        </Suspense>
      ),
    },
  ];
};

export default memberRouter;
