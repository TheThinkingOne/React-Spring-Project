import axios from "axios";

const rest_api_key = "a3e22c5eeab24b7f3385e34e2e13b578"; // 카카오개발자에서 받은 Rest API 키

const redirect_uri = "http://localhost:5173/member/kakao"; // 카카오개발자에서 설정한 리다이렉트 URL

const auth_code_path = "https://kauth.kakao.com/oauth/authorize"; // 카카오 자체에서 설정한 인가 링크

const access_token_uri = "https://kauth.kakao.com/oauth/token"; // 카카오 엑세스 토큰 링크

export const getKakaoLoginLink = () => {
  const kakaoURL = `${auth_code_path}?client_id=${rest_api_key}&redirect_uri=${redirect_uri}&response_type=code`;

  return kakaoURL;
};

export const getAccessToken = async (authCode) => {
  // Ajax로 비동기 통신

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

  const res = await axios.post(access_token_uri, params, header);

  const kakaoAccessToken = res.data.access_token;

  return kakaoAccessToken;
};
