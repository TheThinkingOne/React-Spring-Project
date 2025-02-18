import { Cookies } from "react-cookie";

// 쿠키 설정 관련 유틸
const cookies = new Cookies();

export const setCookie = (name, value, days = 1) => {
  // 날짜처리는 비쥬얼하게
  const expires = new Date();

  expires.setUTCDate(expires.getUTCDate() + days); // 보관기간

  return cookies.set(name, value, { expires: expires, path: "/" }); // 전체 경로(하위 경로)에 쿠키 전역으로 설정해서 로그인 쿠키 저장
}; // 이름, 값, 시간 지정

export const getCookie = (name) => {
  return cookies.get(name);
};

// 내가 어떤 경로에 있는 쿠키를 지워야 하는 상황인지 알아야 하는데 없다면 "/" 에 있는 쿠기를 지우겠다 선언
export const removeCookie = (name, path = "/") => {
  cookies.remove(name, { path: path });
};
