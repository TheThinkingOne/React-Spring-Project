import { Suspense, lazy } from "react";

const Loading = <div>Loading....</div>;

// 로그인과 로그아웃
const Login = lazy(() => import("../pages/member/LoginPage"));
const Logout = lazy(() => import("../pages/member/LogoutPage"));

const KakaoRedirect = lazy(() => import("../pages/member/KaKaoRedirectPage"));

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
      elemet: (
        <Suspense fallback={Loading}>
          <KakaoRedirect />
        </Suspense>
      ),
    },
  ];
};

export default memberRouter;
