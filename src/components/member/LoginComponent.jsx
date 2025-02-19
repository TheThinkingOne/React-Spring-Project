import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { login, loginPostAsync } from "../../slices/loginSlice";
import { loginPost } from "../../api/memberApi";
import { useNavigate } from "react-router-dom";
import useCustomLogin from "../../hooks/useCustomLogin";
import KaKaoLoginComponent from "./KaKaoLoginComponent";

const initState = {
  email: "",
  pw: "",
};

function LoginComponent(props) {
  const [loginParam, setLoginParam] = useState({ ...initState }); // 이메일로그인(oauth)

  const { doLogin, moveToPath } = useCustomLogin();

  // useSelector 와 useDispatch 공부하기
  // dispatch의 내용은 다음에 이 어플리케이션에서 이 데이터를 이렇게 유지해 달라는 다음 데이터

  const handleChange = (e) => {
    loginParam[e.target.name] = e.target.value;

    setLoginParam({ ...loginParam }); // 새로운 객체 만들기
  };

  const handleClickLogin = (e) => {
    //dispatch(login(loginParam));

    doLogin(loginParam).then((data) => {
      if (data.error) {
        alert("이메일과 패스워드를 확인해주세요!");
      } else {
        moveToPath("/");
      }
    });

    // 이 아래껀 로그인 로그인 훅스 적용 전
    // dispatch(loginPostAsync(loginParam))
    //   .unwrap()
    //   .then((data) => {
    //     console.log("after unwrap");
    //     console.log(data); // 비동기 로그인에 쓰는 unwrap
    //     if (data.error) {
    //       alert("이메일과 패스워드를 확인해주세요");
    //     } else {
    //       alert("로그인에 성공했습니다");
    //       navigate({ pathname: "/" }, { replace: true }); // 이렇게 하면 로그인 후에 뒤로가기 막힘
    //     }
    //   }); // 요즘음 createAsyncthunk 설정한 메소드 바로 사용
  };

  return (
    <div className="border-2 border-sky-200 mt-10 m-2 p-4">
      <div className="flex justify-center">
        <div className="text-4xl m-4 p-4 font-extrabold text-blue-500">
          Login Component
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-2/5 p-6 text-right font-bold">Email</div>
          <input
            className="w-1/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md"
            name="email"
            type="text"
            value={loginParam.email}
            onChange={handleChange}
          />
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-2/5 p-6 text-right font-bold">Password</div>
          <input
            className="w-1/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md"
            name="pw"
            type="password"
            value={loginParam.pw}
            onChange={handleChange}
          />
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full justify-center">
          <div className="w-2/5 p-6 flex justify-center font-bold">
            <button
              className="rounded p-4 w-36 bg-blue-500 text-xl text-white"
              onClick={handleClickLogin}
            >
              LOGIN
            </button>
          </div>
        </div>
      </div>

      {/* 2025/02/19 카카오 로그인 추가 */}
      <KaKaoLoginComponent />
    </div>
  );
}

export default LoginComponent;
