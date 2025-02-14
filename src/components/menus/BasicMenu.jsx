import React from "react";
import { useSelector, useDispatch } from "react-redux";
import { login, logout } from "../../slices/loginSlice";
import { Link } from "react-router-dom";

const BasicMenu = () => {
  const dispatch = useDispatch();

  // 🚀 Redux 상태 가져오기 (TypeScript 문법 제거)
  const loginState = useSelector((state) => state.loginSlice);

  // Redux 상태 전체 확인
  console.log(
    "Redux 전체 상태 확인:",
    useSelector((state) => state)
  );
  console.log("loginState 확인:", loginState);

  return (
    <nav id="navbar" className="flex bg-blue-300">
      <div className="w-4/5 bg-gray-500">
        <ul className="flex p-4 text-white font-bold">
          <li className="pr-6 text-2xl">
            <Link to={"/"}>Main</Link>
          </li>
          <li className="pr-6 text-2xl">
            <Link to={"/about"}>About</Link>
          </li>

          {loginState?.email ? (
            <>
              <li className="pr-6 text-2xl">
                <Link to={"/todo/"}>Todo</Link>
              </li>
              <li className="pr-6 text-2xl">
                <Link to={"/products/"}>Products</Link>
              </li>
              <button
                onClick={() => dispatch(logout())}
                className="ml-4 text-white"
              >
                Logout
              </button>
            </>
          ) : (
            <button
              onClick={() => dispatch(login("user@example.com"))}
              className="text-white ml-4"
            >
              Login
            </button>
          )}
        </ul>
      </div>

      {/* 로그인 해서 이메일 값 존재 여부에 따라 로그인 로그아웃 버튼 따로 나타내기 */}
      <div className="w-1/5 flex justify-end bg-orange-300 p-4 font-medium">
        {!loginState.email ? (
          <div className="text-white text-sm m-1 rounded">
            <Link to={"/member/login"}>Login</Link>
          </div>
        ) : (
          <div className="text-white text-sm m-1 rounded">
            <Link to={"/member/logout"}>Logout</Link>
          </div>
        )}
      </div>
    </nav>
  );
};

export default BasicMenu;
