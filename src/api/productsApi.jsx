import axios from "axios";
import jwtAxios from "../util/jwtUtil"; // axios 에서 jwtAxios 로 변경
import { API_SERVER_HOST } from "./todoApi";
import { getCookie } from "../util/cookieUtil";

const host = `${API_SERVER_HOST}/api/products`;

export const postAdd = async (product) => {
  const header = { headers: { "Content-Type": "multipart/form-data" } }; // 요청 헤더 설정(백엔드와 연동)

  const res = await jwtAxios.post(`${host}/`, product, header); // Spring 백엔드로 POST 요청
  // → POST 요청을 보낼 때 **product(FormData 객체)**를 전달하고,
  // → **헤더에 "Content-Type": "multipart/form-data"**를 설정하여 파일 업로드를 지원함.

  return res.data; // 서버응답 데이터 반환(제이슨 데이터)
};

// 스프링의 ProductController 참고, 이와 연동함
export const getList = async (pageParam) => {
  const { page, size } = pageParam; // 구조분해 할당?

  const token = getCookie("member")?.accessToken;

  const headers = {
    Authorization: `Bearer ${token}`,
    "Content-Type": "application/json",
  };

  const res = await jwtAxios.get(`${host}/list`, {
    params: { page: page, size: size },
  });

  return res.data;
};

export const getOne = async (tno) => {
  // 강의는 여기가 pno가 아니고 tno로 되어있다???
  // 여긴 문제 없음
  const res = await jwtAxios.get(`${host}/${tno}`);

  return res.data;
};

export const deleteOne = async (pno) => {
  const res = await jwtAxios.delete(`${host}/${pno}`);

  return res.data;
};

export const putOne = async (pno, product) => {
  const header = { headers: { "Content-Type": "multipart/form-data" } };

  const res = await jwtAxios.put(`${host}/${pno}`, product, header);

  return res.data;
};
