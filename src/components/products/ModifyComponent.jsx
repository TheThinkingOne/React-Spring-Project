import React, { useEffect, useState } from "react";
import { API_SERVER_HOST, deleteOne, getOne, putOne } from "../../api/todoApi";
import FetchingModal from "../common/FetchingModal";
import useCustomMove from "../../hooks/useCustomMove";
import ResultModal from "../common/ResultModal";

const initState = {
  pno: 0,
  pname: "",
  pdesc: "",
  price: "",
  delFlag: false,
  uploadFileNames: [],
};

const host = API_SERVER_HOST;

function ModifyComponent({ pno }) {
  const [product, setProduct] = useState(initState); // 이 문법의 의미? 유즈스테이트 쓰고 없으면 initState 쓰라?

  const [fetching, setFetching] = useState(false);

  const [result, setResult] = useState(false);

  const { moveToList, moveToRead } = useCustomMove();

  const uploadRef = useRef();

  useEffect(() => {
    setFetching(true);

    getOne(pno).then((data) => {
      setProduct(data);
      setFetching(false);
    });
  }, [pno]);

  const handleChangeProduct = (e) => {
    // 입력값 변경하는놈
    product[e.target.name] = e.target.value;
    setProduct({ ...product });
  };

  // 수정에서 삭제할 이미지
  // 수정페이지에서 이미지 삭제버튼 누를때 화면상에서만 삭제되고 저장 누르기 전까지 실제로 삭제되는건 아님
  const deleteOldImages = (imageName) => {
    const resultFileNames = product.uploadFileNames.filter(
      // 이 부분의 로직 좀 더 이해할 필요 있음
      (fileName) => fileName !== imageName
    );

    product.uploadFileNames = resultFileNames;

    setProduct({ ...product });
  };

  const handleClickModify = () => {
    const files = uploadRef.current.files;
    const formData = new FormData();
    for (let i = 0; i < files.length; i++) {
      formData.append("files", files[i]);
    }

    // 수정 페이지에서 새로 넣는 상품 정보 투입
    formData.append("pname", product.pname);
    formData.append("pdesc", product.pdesc);
    formData.append("price", product.price);
    formData.append("delFlag", product.delFlag);

    // 기존에 있었던 파일도 유지한체로 보내줘야 함!! 이 부분이 중요
    for (let i = 0; i < product.uploadFileNames.length; i++) {
      formData.append("uploadFileNames", product.uploadFileNames[i]);
    }

    // 이 부분이 아마 수정창에서 이미지 넣었을 때 새로 나타나게 하는 부분인듯
    putOne(pno, formData).then((data) => {
      setResult("Modified");
      setFetching(false);
    });
  };

  const handleClickDelete = () => {
    setFetching(true);
    deleteOne(pno).then((data) => {
      setResult("Deleted");
      setFetching(false);
    });
  };

  const closeModal = () => {
    if (result === "Modified") {
      moveToRead(pno);
    } else if (result == "Deleted") {
      moveToList({ page: 1 });
    }
    setResult(null);
  };

  return (
    <div className="border-2 border-sky-200 mt-10 m-2 p-4">
      {/* 로딩 중일 때 FetchingModal 표시 */}
      {fetching ? <FetchingModal /> : null}

      {result ? (
        <ResultModal
          title={`${result}`}
          content={"처리되었습니다."}
          callbackFn={closeModal}
        ></ResultModal>
      ) : (
        <></>
      )}

      {/* Product Name */}
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Product Name</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
            name="pname"
            type="text"
            value={product.pname}
            onChange={handleChangeProduct}
          />
        </div>
      </div>

      {/* Description */}
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Desc</div>
          <textarea
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md resize-y"
            name="pdesc"
            rows="4"
            onChange={handleChangeProduct}
            value={product.pdesc}
          />
        </div>
      </div>

      {/* Price */}
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Price</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
            name="price"
            type="number"
            value={product.price}
            onChange={handleChangeProduct}
          />
        </div>
      </div>

      {/* Delete Flag */}
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">DELETE</div>
          <select
            name="delFlag"
            value={product.delFlag}
            onChange={handleChangeProduct}
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
          >
            <option value={false}>사용</option>
            <option value={true}>삭제</option>
          </select>
        </div>
      </div>

      {/* File Upload */}
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Files</div>
          <input
            ref={uploadRef}
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
            type="file"
            multiple
          />
        </div>
      </div>

      {/* Images */}
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Images</div>
          <div className="w-4/5 justify-center flex flex-wrap items-start">
            {product.uploadFileNames.map((imgFile, i) => (
              <div
                className="flex justify-center flex-col w-1/3 m-1 items-center"
                key={i}
              >
                <button
                  className="bg-blue-500 text-white text-lg p-2 rounded mb-2"
                  onClick={() => deleteOldImages(imgFile)}
                >
                  DELETE
                </button>
                <img
                  alt="img"
                  className="rounded shadow-md"
                  src={`${host}/api/products/view/s_${imgFile}`}
                />
              </div>
            ))}
          </div>
        </div>

        {/* // 수정, 삭제, 리스트 목록 버튼 */}
        <div className="flex justify-end p-4">
          <button
            type="button"
            className="rounded p-4 m-2 text-xl w-32 text-white bg-red-500"
            onClick={handleClickDelete}
          >
            Delete
          </button>

          <button
            type="button"
            onClick={handleClickModify}
            className="inline-block rounded p-4 m-2 text-xl w-32 text-white bg-orange-500"
          >
            Modify
          </button>

          <button
            type="button"
            className="rounded p-4 m-2 text-xl w-32 text-white bg-blue-500"
            onClick={() => moveToList}
            // 파라미터 없이 전달시킴
          >
            List
          </button>
        </div>
      </div>
    </div>
  );
}

export default ModifyComponent;
