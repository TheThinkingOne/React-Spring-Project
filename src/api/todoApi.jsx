// ajax 통신하는 함수 작성
// api jsx 파일은 API 호출과 관련된 함수 및 경로 설정을 관리하는 역할
// GET PUT DELETE 같은 CRUD에 필요한 함수 작성

import axios from "axios";

// 경로 설정
// api 서버 작업
// 비동기 통신이란 서버로 데이터를 요청한 후,
// 응답이 올 때까지 기다리지 않고 다른 작업을 진행할 수 있는 방식입니다.

export const API_SERVER_HOST = "http://localhost:8080"; // api 서버의 기본 URL 설정
// 스프링 어플리케이션 실행하니까 게시글 조회 페이지에 tno 값이 제대로 뜬다.

const prefix = `${API_SERVER_HOST}/api/todo`; // API 요청의 기본 경로 설정
// prefix : 모든 API 요청은 /api/todo 경로를 기준으로 진행

// 비동기 통신

export const getOne = async (tno) => {
  // async = 비동기통신
  const res = await axios.get(`${prefix}/${tno}`); // tno로 get 요청

  return res.data;
};

export const getList = async (PageParam) => {
  const { page, size } = PageParam;

  const res = await axios.get(`${prefix}/list`, { params: { page, size } });
  // PageParam에서 page(페이지 번호)와 size(페이지 크기)를 추출

  // async 의 모든 리턴값은 비동기이다.(Promise 객체)
  // await 를 사용하면 Promise가 해결된 값(실제 데이터)을 반환함
  return res.data;

  // axios와 async/await 장점
  // 1. 간결한 비동기 처리, 유지보수성, 확장성

  // 유즈 스테이트?
};

export const postAdd = async (todoObj) => {
  // JSON.stringify(obj) => 어쩌구 이런거 할필요 없음 axios 사용하면
  const res = await axios.post(`${prefix}/`, todoObj);

  return res.data;
};

export const deleteOne = async (tno) => {
  // 게시글 삭제
  const res = await axios.delete(`${prefix}/${tno}`);

  return res.data;
};

export const putOne = async (tno) => {
  // 게시글 수정
  const res = await axios.put(`${prefix}/${todo.tno}`, todo);

  return res.data;
};
