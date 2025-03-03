import axios from "axios";
import { API_SERVER_HOST } from "./todoApi";

const rest_api_key = "a3e22c5eeab24b7f3385e34e2e13b578"; // 카카오개발자에서 받은 Rest API 키

const redirect_uri = "http://localhost:5173/member/kakao"; // 카카오개발자에서 설정한 리다이렉트 URL 이건 문제없는듯함

const auth_code_path = "https://kauth.kakao.com/oauth/authorize"; // 카카오 자체에서 설정한 인가 링크

const access_token_uri = "https://kauth.kakao.com/oauth/token"; // 카카오 엑세스 토큰 링크

const secret_key = "YChReJ7bS7y7qDgz1PPT3rYwSzV0ieI9";

export const getKakaoLoginLink = () => {
  const kakaoURL = `${auth_code_path}?client_id=${rest_api_key}&redirect_uri=${redirect_uri}&response_type=code`;

  return kakaoURL;
};

export const getAccessToken = async (authCode) => {
  // Ajax로 비동기 통신
  console.log("[DEBUG용도] getAccessToken 호출됨, authCode:", authCode);

  // 1. 헤더 지정
  const header = {
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=utf-8",
    },
  };

  const params = {
    //카카오 엑세스 토큰 받기에 필요한 쿼리 파라미터(사진 참고)
    grant_type: "authorization_code",
    client_id: rest_api_key,
    redirect_uri: redirect_uri,
    code: authCode,
  };

  // const res = await axios.post(access_token_uri, params, header);

  // const accessToken = res.data.access_token; // 원래 내가 변수명을 kakaoAccessToken 으로 해놨었는데 강의랑 똑같이 accessToken 으로 변경해봄

  // return accessToken;
  try {
    const res = await axios.post(access_token_uri, params, header);
    const accessToken = res.data.access_token;
    console.log("[DEBUG] 카카오에서 받은 accessToken:", accessToken);
    return accessToken;
  } catch (error) {
    console.error("[ERROR] 카카오 accessToken 요청 실패:", error);
  }
};

export const getMemberWithAccessToken = async (accessToken) => {
  const res = await axios.get(
    `${API_SERVER_HOST}/api/member/kakao?accessToken=${accessToken}`
  );
  return res.data;
};
