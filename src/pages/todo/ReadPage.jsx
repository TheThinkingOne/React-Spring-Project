import React from "react";
import {
  createSearchParams,
  useNavigate,
  useParams,
  useSearchParams,
} from "react-router-dom";
import ReadComponent from "./ReadComponent";

// function ReadPage(props) {

//     // 동적이동 처리
//     const navigate = useNavigate();
//     const {tno} = useParams(); // 리액트의 훅스 기능

//     console.log("URL에서 가져온 tno 값:", tno);

//     const [queryParams] = useSearchParams()

//     const page = queryParams.get('page') ? parseInt(queryParams.get('page')) : 1
//     const size = queryParams.get('size') ? parseInt(queryParams.get('size')) : 10

//     // createSearchParam 이용하면 물음표 뒤에 쿼리 스트링 만들어줌
//     const queryStr = createSearchParams({page:page,size:size}).toString()

//     console.log(tno)

//     const moveToModify = (tno) => { // tno 를 파라미터로 받아서
//         navigate({
//             pathname:`/todo/modify/${tno}`,
//             search: queryStr
//         })
//     }

//     //
//     const moveToList = () => {
//         navigate({
//             pathname:`/todo/list`,
//             search: queryStr
//         })
//     }

//     return (
//         // <div className={'text-3xl'}>
//         //     Todo Read Page {tno}

//         //     <div>
//         //         <button onClick={() => moveToModify(tno)}> Test Modify </button>
//         //         <button onClick={moveToList}> Test List </button>
//         //     </div>

//         // </div>
//         <div className="font-extrabold w-full bg-white mt-6">

//             <div className="text-2xl">
//                 Todo Read Page Component {tno}
//             </div>

//             <ReadComponent tno={tno}/>

//         </div>
//     );
// }

const ReadPage = () => {
  const { tno } = useParams();

  console.log("tno:", tno);

  // 컴포넌트 중심으로 개편

  return (
    <div className="font-extrabold w-full bg-white mt-6">
      <div className="text-2xl">Todo Read Page Component {tno}</div>

      <ReadComponent tno={tno} />
    </div>
  );
};

export default ReadPage;
