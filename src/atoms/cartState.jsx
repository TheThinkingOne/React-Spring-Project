import { current } from "@reduxjs/toolkit";
import { atom, selector } from "recoil";

export const cartState = atom({
  key: "cartState",
  default: [],
});

export const cartTotalState = selector({
  key: "cartTotalState",
  get: ({ get }) => {
    // 장바구니 값 계산 로직
    const arr = get(cartState);

    const initialValue = 0;

    return arr.reduce(
      (total, current) => total + current.price * current.qty,
      initialValue
    );
  },
});
