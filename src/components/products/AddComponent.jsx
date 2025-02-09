import React, { useRef, useState } from "react";
import { postAdd } from "../../api/productApi";
import FetchingModal from "../common/FetchingModal";
import ResultModal from "../common/ResultModal";
import useCustomMove from "../../hooks/useCustomMove";

const initState = {
  // 상품 정보에 관한 초기 배열
  pname: "",
  pdesc: "",
  price: 0,
  files: [],
};

// Multipart Form Data 사용 필
// new FormData() -> POST, PUT

function AddComponent(props) {
  // 상품 데이터 정의

  const [product, setProduct] = useState(initState);

  const uploadRef = useRef();

  const [fetching, setFetching] = useState(false); // 이건 뭐징

  const [result, setResult] = useState(false); // 결과끝나면 모달창 보여지게 하기

  const { moveToList } = useCustomMove();

  // multipart/form-data FormData()

  const handleChangeProduct = (e) => {
    // 입력값 변경하는놈
    product[e.target.name] = e.target.value;
    setProduct({ ...product });
  };

  const handleClickAdd = (e) => {
    console.log(product);

    const formData = new FormData();

    const files = uploadRef.current.files;

    console.log(files);

    // 파일이 몇개 올라갔는지 확인가능
    console.log(files.length);

    // 상품정보 업로드 할때 전송되는 파일 정보들
    for (let i = 0; i < files.length; i++) {
      formData.append("files", files[i]);
    }
    formData.append("pname", product.pname);
    formData.append("pdesc", product.pdesc);
    formData.append("price", product.price);
    //formData.append("pname", product.pname);
    console.log(formData);

    setFetching(true);

    postAdd(formData).then((data) => {
      setFetching(false);
      console.log("postAdd 서버응답값 : ", data);
      setResult(data.RESULT);
    });
  };

  const closeModal = () => {
    setResult(null);
    moveToList({ page: 1 });
  };

  return (
    <div className="border-2 border-sky-200 mt-10 m-2 p-4">
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

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Desc</div>
          <textarea
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md resize-y"
            name="pdesc"
            rows="4"
            value={product.pdesc}
            onChange={handleChangeProduct}
          >
            {product.pdesc}
          </textarea>
        </div>
      </div>

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

      <div className="flex justify-end">
        <div className="relative mb-4 flex p-4 flex-wrap items-stretch">
          <button
            type="button"
            className="rounded p-4 w-36 bg-blue-500 text-xl text-white"
            onClick={handleClickAdd}
          >
            ADD
          </button>
        </div>
      </div>

      {fetching ? <FetchingModal /> : <></>}

      {result ? (
        <ResultModal
          callbackFn={closeModal}
          title={"Product Add Result"}
          content={`${result}번 상품 등록 완료`}
        ></ResultModal>
      ) : (
        <></>
      )}
    </div>
  );
}

export default AddComponent;
