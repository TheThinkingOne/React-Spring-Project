import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { modifyMember } from "../../api/memberApi";
import useCustomLogin from "../../hooks/useCustomLogin";
import ResultModal from "../common/ResultModal";

const initState = {
  email: "",
  pw: "",
  nickname: "",
};

function ModifyComponent(props) {
  const [member, setMember] = useState(initState);

  // 정보 끄집어내기
  const loginInfo = useSelector((state) => state.loginSlice); // 사용자 상태 끄집어내기

  const { moveToLogin } = useCustomLogin();

  const [result, setResult] = useState();

  useEffect(() => {
    setMember({ ...loginInfo, pw: "ABCD" }); // 상태 변경하기, 처음 로그인 했을 때 비밀번호는 ABCD 로 통일
  }, [loginInfo]);

  const handleChange = (e) => {
    member[e.target.name] = e.target.value;

    setMember({ ...member });
  };

  // 사용자 회원정보 수정
  const handleClickModify = () => {
    modifyMember(member).then((result) => {
      setResult("Modified!");
    });
  };

  // 모달창 닫고 로그인 페이지로 보내기(사용자 정보 변경 후에)
  const closeModal = () => {
    setResult(null);
    moveToLogin();
  };

  return (
    <div className="mt-6">
      {result ? (
        <ResultModal
          callbackFn={closeModal}
          title={"회원 정보 수정"}
          content={"회원정보 수정 완료. 다시 로그인 해주세요."}
        ></ResultModal>
      ) : (
        <></>
      )}

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Email</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
            name="email"
            type={"text"}
            value={member.email}
            readOnly
          ></input>
        </div>
      </div>
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Password</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
            name="pw"
            type={"password"}
            value={member.pw}
            onChange={handleChange}
          ></input>
        </div>
      </div>
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Nickname</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
            name="nickname"
            type={"text"}
            value={member.nickname}
            onChange={handleChange}
          ></input>
        </div>
      </div>
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap justify-end">
          <button
            type="button"
            className="rounded p-4 m-2 text-xl w-32 text-white bg-blue-500"
            onClick={handleClickModify}
          >
            {" "}
            Modify{" "}
          </button>
        </div>
      </div>
    </div>
  );
}

export default ModifyComponent;
