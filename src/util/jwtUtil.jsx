import axios from "axios";
import { getCookie, setCookie } from "./cookieUtil.jsx";
import { API_SERVER_HOST } from "../api/todoApi";
import { Await } from "react-router-dom";

const jwtAxios = axios.create();

const refreshJWT = async (accessToken, refreshToken) => {
  //
  const host = API_SERVER_HOST;

  const header = { headers: { Authorization: `Bearer ${accessToken}` } };
  // const header = { headers: { Authorization: `Bearer ${accessToken}` } }; // 여기 강의 보고 수정하기 전에

  const res = await axios.get(
    `${host}/api/member/refresh?refreshToken=${refreshToken}`,
    header
  );

  console.log(res.data); // 여기서 나오는 데이터는 새로 만들어진 엑세스 토큰과 새로 만들어진 Response 토큰이다.

  return res.data; // 여기서 나오는 데이터가 뭔지 알아야 한다
};

//before request
const beforeReq = (config) => {
  // 이부분 집중적으로 알아보자

  // 여기 하고나니까 로그인하고 Todo 들어가짐
  console.log("before request.............");

  const memberInfo = getCookie("member");

  console.log("memberInfo from cookie:", memberInfo); // 🔍 쿠키에서 토큰이 정상적으로 있는지 확인

  if (!memberInfo) {
    // 로그인 되어있는 상태가 아니라면
    console.log("Member not Found");
    return Promise.reject({ response: { data: { error: "RETURN_LOGIN" } } });
  }

  const { accessToken } = memberInfo;
  console.log("JWTCheckUtil.jsx 에서 확인한 토큰 :", accessToken); // 🔍 실제로 저장된 JWT 토큰 확인

  console.log(
    "-------------------------------------------------" + accessToken
  );

  config.headers.Authorization = `Bearer ${accessToken}`;

  return config;
};

//fail request , 응답이 잘못되었을 경우
const requestFail = (err) => {
  console.log("request error............");
  return Promise.reject(err);
};

//before return response
const beforeRes = async (res) => {
  console.log("before return response...........");

  // 데이터는 있는데 에러가 있는경우(엑세스 토큰 쪽)
  const data = res.data;

  if (data && data.error === "ERROR WITH ACCESS TOKEN LUL") {
    console.log("------------------------------123456789");
    // data.error 를 아마 Spring 코드에 쓴 그대로 적어야 할 것
    // !! 클라이언트(리액트쪽)와 서버쪽(스프링)의 에러메세지가 반드시 동일해야 함
    // 쿠키에서 정보 꺼내서
    const memberCookieValue = getCookie("member");

    const result = await refreshJWT(
      memberCookieValue.accessToken,
      memberCookieValue.refreshToken
    );

    // 정상적인 경우라면 새로운 엑세스 토큰과 리프레시 토큰이 올것
    // 새로 나온 토큰을 쿠키에 갱신, 쿠키 자체도 갱신
    memberCookieValue.accessToken = result.accessToken;
    memberCookieValue.refreshToken = result.refreshToken;

    setCookie("member", JSON.stringify(memberCookieValue), 1); // 지속적으로 쿠키값을 바꿔가면서 정보를 유지하는것

    const originalRequest = res.config;

    originalRequest.headers.Authorization = `Bearer ${result.accessToken}`; // 새로 갱신된 엑세스 토큰으로 교체

    return await axios(originalRequest); // 갱신된 토큰 보내기 => 이러면 우리가 원한대로 자동으로 토큰이 갱신됨

    // 이제 다른 브라우저 창에서 열어서도 토큰이 유효한 동안 바로 로그인 된 상태로 사이트 열람 가능
  }

  return res;
};

//fail response
const responseFail = (err) => {
  console.log("response fail error.............");

  return Promise.reject(err);
};

//
jwtAxios.interceptors.request.use(beforeReq, requestFail);
jwtAxios.interceptors.response.use(beforeRes, responseFail);

export default jwtAxios;
