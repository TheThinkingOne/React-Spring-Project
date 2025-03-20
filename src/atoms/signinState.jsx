import { atom } from "recoil";
import { getCookie } from "../util/cookieUtil";

const initState = {
  email: "",
  nickname: "",
  social: false,
  accessToken: "",
  refreshToken: "",
};

const loadMemberCookie = () => {
  const memberInfo = getCookie("member");

  // 한글닉네임 관련 처리 ]
  if (memberInfo && memberInfo.nickname) {
    memberInfo.nickname = decodeURIComponent(memberInfo.nickname);
  }

  return memberInfo;
};

export const signinState = atom({
  key: "signinState",
  default: loadMemberCookie() || initState, // 쿠기 있으면 왼쪽으로 로그인, 없으면 빈창
});
