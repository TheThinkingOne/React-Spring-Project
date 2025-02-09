import React from "react";
import ReadComponent from "../../components/products/ReadComponent";

function ReadPage(props) {
  // Read 뒤에있는 상품 번호 추출
  return (
    <div className="p-4 w-full bg-white">
      <div className="text-3xl font-extrabold">Products Read Page</div>
      <ReadComponent pno={pno}></ReadComponent>
    </div>
  );
}
// 페이지 만든다음엔 라우터 ㄱㄱㄱ
export default ReadPage;
