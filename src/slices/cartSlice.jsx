import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { getCartItems, postChangeCart } from "../api/CartApi";

// createAsyncThunk 로 감싼 이유 : extra reducer 로 동작시키기 위함
export const getCartItemsAsync = createAsyncThunk("getCartItemsAsync", () => {
  return getCartItems();
});

export const postChangeCartAsync = createAsyncThunk(
  "postChangeCartAsync",
  (param) => {
    return postChangeCart(param); //
  }
);

const initState = []; // 사용자가 장바구니 만든적 없으면 빈 배열로 나옴

// const cartSlice = createSlice({
//   name: "cartSlice",
//   initialState: initState,

//   extraReducers: (builder) => {
//     builder.addCase(getCartItemsAsync.fulfilled, (state, action) => {
//       //
//       console.log("getCartItemsAsync.fulfilled");
//       //
//       console.log("장바구니 목록 :" + action.payload);
//       return action.payload; // 페이로드 자체가 해당 사용자의 장바구니의 아이템
//     });
//         .addCase(postChangeCartAsync.fulfilled, (state, action) => {
//             //
//             console.log("postChangeCartAsync.fulfilled")

//             return action.payload
//         })
//   },
// });

const cartSlice = createSlice({
  name: "cartSlice",
  initialState: initState,

  extraReducers: (builder) => {
    builder
      .addCase(getCartItemsAsync.fulfilled, (state, action) => {
        console.log("getCartItemsAsync.fulfilled");
        console.log("장바구니 목록:", action.payload);
        return action.payload; // payload 자체가 장바구니 아이템 목록
      })
      .addCase(postChangeCartAsync.fulfilled, (state, action) => {
        console.log("postChangeCartAsync.fulfilled");
        return action.payload;
      });
  },
});

export default cartSlice.reducer;

// 여기도 다시 확인
