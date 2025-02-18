import React from "react";
import { Link } from "react-router-dom";
import BasicLayout from "../layouts/BasicLayout.jsx";
import useCustomLogin from "../hooks/useCustomLogin.jsx";

const AboutPage = () => {
  const { isLogin, moveToLoginReturn } = useCustomLogin(); // 로그인 여부 확인

  if (!isLogin) {
    return moveToLoginReturn(); // 로그인 한 상황이 아니면 네비게이트만 리턴
  }

  return (
    <BasicLayout>
      <div className={"text-3xl"}>Main Page</div>
    </BasicLayout>
  );
};

export default AboutPage;
