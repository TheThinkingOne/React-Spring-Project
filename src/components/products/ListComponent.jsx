import React, { useEffect, useState } from "react";
import useCustomMove from "../../hooks/useCustomMove";
import { API_SERVER_HOST } from "../../api/todoApi";
import { getList } from "../../api/productsApi";
import FetchingModal from "../common/FetchingModal";
import PageComponent from "../common/PageComponent";
import { getCookie } from "../../util/cookieUtil"; // 쿠키에서 토큰 가져오기
import App from "../../App.jsx";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import useCustomLogin from "../../hooks/useCustomLogin";

const initState = {
  dtoList: [],
  pageNumList: [],
  pageRequestDto: null,
  prev: false,
  next: false,
  totalCount: 0,
  prevPage: 0,
  nextPage: 0,
  totalPage: 0,
  current: 0,
};

const ListComponent = () => {
  const { moveToList, moveToRead, page, size, refresh } = useCustomMove();

  const { moveToLogin } = useCustomLogin();

  const { exceptionHandle } = useCustomLogin();

  const host = API_SERVER_HOST;

  const { data, isFetching, error, isError } = useQuery({
    // 이 부분에서 오류나는중 noQueryClient set 오류
    queryKey: ["products/list", { page, size, refresh }],
    // 이렇게하면 계속클릭했을때 서버가 계속 호출하는 부담 줄일수있음
    queryFn: () => getList({ page, size }),
    staleTime: 1000 * 60, // 60초동안은 동일페이지 클릭문제 꽤 해결
  });

  const queryClient = useQueryClient();

  const handleClickPage = (pageParam) => {
    // if (pageParam.page === parseInt(page)) {
    //   queryClient.invalidateQueries("products/list"); // 해당 경로의 쿼리를 모두 무효화 시킴
    // }

    moveToList(pageParam);
  };

  const serverData = data || initState;

  // const [serverData, setServerData] = useState(initState);
  // const [fetching, setFetching] = useState(false); // 리액트 쿼리 쓰면서 이 부분 안 써도됨

  const token = getCookie("member")?.accessToken;

  // 리액트 쿼리 쓰면서 이 부분 안써도 됨
  // useEffect(() => {
  //   setFetching(true);

  //   getList({ page, size }).then((data) => {
  //     console.log("[DEBUG] 서버에서 받은 데이터:", data);
  //     setFetching(false);
  //     setServerData(data);
  //   });
  // }, [page, size, refresh]);

  return (
    <div className="border-2 border-blue-100 mt-10 mr-2 ml-2">
      {/* 로딩 중일 때 FetchingModal 표시 */}
      {isFetching ? <FetchingModal /> : <></>}

      <div className="flex flex-wrap mx-auto p-6">
        {serverData.dtoList.map((product) => (
          <div
            key={product.pno}
            className="w-1/2 p-1 rounded shadow-md border-2 cursor-pointer"
            onClick={() => moveToRead(product.pno)}
          >
            <div className="flex flex-col h-full">
              {/* 상품 번호 */}
              <div className="font-extrabold text-2xl p-2 w-full">
                {product.pno}
              </div>

              {/* 이미지 영역 */}
              <div className="text-1xl m-1 p-2 w-full flex flex-col">
                <div className="w-full overflow-hidden flex flex-col items-center">
                  <img
                    alt="product"
                    className="rounded-md w-60"
                    src={`${host}/api/products/view/s_${product.uploadFileNames[0]}`}
                    style={{
                      headers: { Authorization: `Bearer ${token}` }, // ✅ Authorization 헤더 추가
                    }}
                  />
                </div>

                {/* 상품 정보 (이름 & 가격) */}
                <div className="bottom-0 font-extrabold bg-white text-center">
                  <div className="p-1">이름: {product.pname}</div>
                  <div className="p-1">가격: {product.price}</div>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>
      <PageComponent serverData={serverData} movePage={handleClickPage} />
    </div>
  );
};

export default ListComponent;
