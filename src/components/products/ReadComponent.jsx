import React, { useEffect, useState } from "react";
import { API_SERVER_HOST } from "../../api/todoApi";
import FetchingModal from "../common/FetchingModal";
import useCustomMove from "../../hooks/useCustomMove.jsx";
import { getOne } from "../../api/productsApi.jsx"; // 여기에 getOne 이라는 함수를 수정 전에 todoApi 에서 불러오고 있었음 아마 잘못 적은듯
import useCustomCart from "../../hooks/useCustomCart.jsx";
import useCustomLogin from "../../hooks/useCustomLogin.jsx";
import { useQuery } from "@tanstack/react-query";

const initState = {
  // 여긴 문제 없음
  pno: 0,
  pname: "",
  pdesc: "",
  price: 0, // 여기 원래 "" 로 되어 있었떤데 오타인가
  uploadFileNames: [],
};

const host = API_SERVER_HOST;

function ReadComponent({ pno }) {
  // pno를 파라미터로
  // 리액트 쿼리 사용함으로써 이부분 사용 안함
  // const [product, setProduct] = useState(initState);

  // 리액트 쿼리 사용함으로써 이부분 사용 안함
  // const [fetching, setFetching] = useState(false);

  const { moveToList, moveToModify, page, size } = useCustomMove();

  // 현재 사용자의 장바구니 아이템들을 얻어옴
  const { cartItems, changeCart } = useCustomCart();

  const { loginState } = useCustomLogin();

  // 버전5에선 파라미터가 객체로 처리
  // 유즈쿼리 쓰면 useEffect 부분이 간결해짐
  const { data, isFetching } = useQuery({
    queryKey: ["products", pno], // 자바의 hashMap 같은건가
    queryFn: () => getOne(pno),
    staleTime: 1000 * 10, // 유통기한이라고 보면? => 10초동안 서버를 다시 호출하지 않음
    // 이렇게 해서 서버 리소스 아끼는거임
  });

  // 리액트 쿼리 쓰기전에 이런거 썼는데 쿼리 쓰면 이런 처리가 간단해지는 것인가
  // useEffect(() => {
  //
  //   setFetching(true);

  //   getOne(pno).then((data) => {
  //     console.log(data);
  //     setProduct(data);
  //     setFetching(false);
  //   });
  // }, [pno]);

  const handleClickAddCart = () => {
    let qty = 1;

    // 필터링
    const addedItem = cartItems.filter((item) => item.pno === parseInt(pno))[0]; // 이미 추가된 아이템

    // 이미 장바구니에 들어있는 상품인데 사용자가 또 추가하는 경우
    if (addedItem) {
      if (
        window.confirm("이미 추가된 상품입니다. 더 추가하시겠습니까?") === false
      ) {
        return;
      }
      qty = addedItem.qty + 1;
    }

    changeCart({ email: loginState.email, qty: qty, pno: pno }); // cino 없이 가능
  };

  // 상태관리로 했었는데 리액트 쿼리 쓰면 그럴필요 없음
  const product = data || initState;

  return (
    <div className="border-2 border-sky-200 mt-10 m-2 p-4">
      {/* 로딩 중일 때 FetchingModal 표시 */}
      {isFetching ? <FetchingModal /> : <></>}

      {/* 상품 정보 */}
      <div className="flex justify-center mt-10">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">PNO</div>
          <div className="w-4/5 p-6 rounded-r border border-solid shadow-md">
            {product.pno}
          </div>
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">PNAME</div>
          <div className="w-4/5 p-6 rounded-r border border-solid shadow-md">
            {product.pname}
          </div>
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">PRICE</div>
          <div className="w-4/5 p-6 rounded-r border border-solid shadow-md">
            {product.price}
          </div>
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">PDESC</div>
          <div className="w-4/5 p-6 rounded-r border border-solid shadow-md">
            {product.pdesc}
          </div>
        </div>
      </div>

      {/* 이미지 리스트 */}
      <div className="w-full flex flex-col items-center">
        {product.uploadFileNames.map((imgFile, i) => (
          <img
            alt="product"
            key={i}
            className="p-4 w-1/2 rounded-md"
            src={`${host}/api/products/view/${imgFile}`}
          />
        ))}
      </div>

      {/* 버튼들 */}
      <div className="flex justify-end p-4">
        <button
          type="button"
          className="inline-block rounded p-4 m-2 text-xl w-32 text-white bg-green-500"
          onClick={handleClickAddCart}
        >
          Add to Cart
        </button>

        <button
          type="button"
          className="inline-block rounded p-4 m-2 text-xl w-32 text-white bg-red-500"
          onClick={() => moveToModify(pno)}
        >
          Modify
        </button>

        <button
          type="button"
          className="rounded p-4 m-2 text-xl w-32 text-white bg-blue-500"
          onClick={() => moveToList({ page, size })}
        >
          List
        </button>
      </div>
    </div>
  );
}

export default ReadComponent;
