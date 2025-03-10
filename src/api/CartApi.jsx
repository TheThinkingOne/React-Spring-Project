import axios from "axios";
import { API_SERVER_HOST } from "./todoApi";
import jwtAxios from "../util/jwtUtil";

const host = `${API_SERVER_HOST}/api/cart`;

export const getCartItems = async () => {
  const res = await jwtAxios.get(`${host}/items`);

  return res.data;
};

export const postChangeCart = async (cartItem) => {
  const res = await jwtAxios.post(`${host}/change`, cartItem); // JSON 으로 데이터 보냄

  return res.data;

  // 모든 데이터를 리덕스 같은거로 관리하려는 생각은 위험하다
};
