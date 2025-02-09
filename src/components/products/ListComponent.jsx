import React, { useEffect, useState } from "react";
import useCustomMove from "../../hooks/useCustomMove";
import { API_SERVER_HOST, getList } from "../../api/todoApi";
import FetchingModal from "../common/FetchingModal";
import PageComponent from "../common/PageComponent";

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

const host = API_SERVER_HOST;

function ListComponent(props) {
  const { moveToList, moveToRead, page, size, refresh } = useCustomMove();

  const [serverData, setServerData] = useState(initState);

  const [fetching, setFetching] = useState(false);

  useEffect(() => {
    // 이부분 다시 공부해야겠어 useEffect 에 대해 알아보기
    setFetching(true);

    getList({ page, size }).then((data) => {
      setFetching(true);
      setServerData(data);
    });
  }, [page, size, refresh]);

  return (
    <div className="border-2 border-blue-100 mt-10 mr-2 ml-2">
      {/* 로딩 중일 때 FetchingModal 표시 */}
      {fetching ? <FetchingModal /> : null}

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
                <div className="w-full overflow-hidden flex justify-center">
                  <img
                    alt="product"
                    className="rounded-md w-60"
                    src={`${host}/api/products/view/s_${product.uploadFileNames[0]}`} // 섬네일보여주는부분
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
      <PageComponent
        serverData={serverData}
        movePage={moveToList}
      ></PageComponent>
    </div>
  );
}

export default ListComponent;
