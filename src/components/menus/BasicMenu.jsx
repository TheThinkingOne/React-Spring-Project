import React from "react";
import { useSelector } from "react-redux";
//import { login, logout } from "../slices/loginSlice.jsx"; // 경로에 맞게 설정
import { Link } from "react-router-dom";

const BasicMenu = () => {
  // 리듀서 사용해서 로그인, 로그아웃 여부에 따라 화면 다르게 나타낼것?
  const loginState = useSelector((state) => state.loginSlice); // 데이터가 바뀌면 이쪽에서 통지를 받는다
  // const loginState = useSelector()
  console.log(
    "전체 상태 확인:",
    useSelector((state) => state)
  );

  // 이부분 왜자꾸 쳐안되는거지
  // 로그인 슬라이서가 가지고 있는 로그인상태
  console.log("loginState ======= " + loginState);

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

          {/* Todo와 Product는 로그인한 사용자만 볼 수 있게 할것 */}
          {loginState.email ? ( //로그인한 사용자만 출력되는 메뉴
            <>
              <li className="pr-6 text-2xl">
                <Link to={"/todo/"}>Todo</Link>
              </li>
              <li className="pr-6 text-2xl">
                <Link to={"/products/"}>Products</Link>
              </li>
            </>
          ) : (
            <></>
          )}
          {/* Todo와 Product는 로그인한 사용자만 볼 수 있게 할것 */}
          {}
        </ul>
      </div>
      <div className="w-1/5 flex justify-end bg-orange-300 p-4 font-medium">
        <div className="text-white text-sm rounded">
          <Link to={"/member/login"}>Login</Link>
        </div>
      </div>
    </nav>
  );
};

export default BasicMenu;
