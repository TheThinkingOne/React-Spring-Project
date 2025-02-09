import React, { useState } from "react";
import ResultModal from "../../components/common/ResultModal";
import { postAdd } from "../../api/todoApi";
import useCustomMove from "../../hooks/useCustomMove";

const initState = {
  title: "",
  writer: "",
  dueDate: "",
};

function AddComponent(props) {
  const [todo, setTodo] = useState({ ...initState }); // 상태코드

  const [result, setResult] = useState(null);

  const { moveToList } = useCustomMove(); // 새 글이 등록되면 1페이지로 이동

  // 변경에 대한 처리
  const handleChangeTodo = (e) => {
    // todo[title]

    console.log(e.target.name, e.target.value);
    todo[e.target.name] = e.target.value; // 이게 무슨 작성법이징

    setTodo({ ...todo });
    // 문법이 익숙하지 않아 잘 모르겠군
  };

  const handleClicked = () => {
    //console.log(todo);
    postAdd(todo).then((result) => {
      // {TNO:104} // 이런 형태로 나올것임
      setResult(result.TNO);
      setTodo({ ...initState }); // 초기화
    }); // todo 전달
  };

  const closeModal = () => {
    setResult(null); // 모달창 안나오게하기
    moveToList(); // 파라미터 없으면 1페이지로 이동하게 됨
  };

  return (
    <div className="border-2 border-sky-200 mt-10 m-2 p-4">
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">TITLE</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md"
            name="title"
            type="text"
            value={todo.title}
            onChange={handleChangeTodo}
          />
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">WRITER</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md"
            name="writer"
            type="text"
            value={todo.writer}
            onChange={handleChangeTodo}
          />
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">DUEDATE</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md"
            name="dueDate"
            type="date"
            value={todo.dueDate}
            onChange={handleChangeTodo}
          />
        </div>
      </div>

      <div className="flex justify-end">
        <div className="relative mb-4 flex p-4 flex-wrap items-stretch">
          <button
            type="button"
            onClick={handleClicked}
            className="rounded p-4 w-36 bg-blue-500 text-xl text-white"
          >
            ADD
          </button>
        </div>
      </div>

      {result ? (
        <ResultModal
          title={"Add Result"}
          content={`New ${result} Added`}
          callbackFn={closeModal}
        ></ResultModal>
      ) : (
        <></>
      )}
      {/* 만약 게시글을 써서 결과가 있으면 ResultModal 을 띄우기, 아니면 아무일도 안 일어나게 */}
    </div>
  );
}

export default AddComponent;
