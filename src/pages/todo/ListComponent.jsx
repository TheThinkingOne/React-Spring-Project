import React, { useEffect, useState } from "react";
import useCustomMove from "../../hooks/useCustomMove";
import { getList } from "../../api/todoApi";
import PageComponent from "../../components/common/PageComponent";

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

function ListComponent(props) {
  // ajax 통신은 useComponent, useEffect
  const { page, size, refresh, moveToList, moveToRead } = useCustomMove();

  const [serverData, setServerData] = useState(initState);

  useEffect(() => {
    getList({ page, size }).then((data) => {
      console.log(data);
      setServerData(data);
    });
  }, [page, size]); // 기본적으론 페이지나 사이즈가 변하지 않으면 서버 새로 호출 안함

  return (
    <div className="border-2 border-blue-100 mt-10 mr-2 ml-2 max-w-[1200px] mx-auto">
      <div className="flex flex-wrap justify-center gap-4 w-full p-6">
        {serverData.dtoList.map((todo) => (
          <div
            key={todo.tno}
            className="basis-[30%] min-w-[300px] p-4 rounded shadow-md bg-white cursor-pointer"
            onClick={() => moveToRead(todo.tno)}
          >
            <div className="flex justify-between">
              <div className="font-extrabold text-2xl p-2">{todo.tno}</div>
              <div className="text-1xl m-1 p-2 font-extrabold">
                {todo.title}
              </div>
              <div className="text-1xl m-1 p-2 font-medium">{todo.dueDate}</div>
            </div>
          </div>
        ))}
      </div>

      <PageComponent serverData={serverData} movePage={moveToList} />
    </div>
  );
}

export default ListComponent;
