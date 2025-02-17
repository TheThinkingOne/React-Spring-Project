// createAsyncthunk 로 로그인 유지

import axios from "axios";
import { API_SERVER_HOST } from "./todoApi";

//
const host = `${API_SERVER_HOST}/api/member`;

export const loginPost = async (loginParam) => {
  const header = { headers: { "Content-Type": "x-www-form-urlencoded" } };
  const form = new FormData();

  form.append("username", loginParam.email); // 이메일과 비번 전달
  form.append("password", loginParam.pw);

  const res = await axios.post(`${host}/login`, form, header);
  //
  return res.data;
};
