import React, { useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { getAccessToken, getMemberWithAccessToken } from "../../api/kakaoApi";
import { useDispatch } from "react-redux";
import useCustomLogin from "../../hooks/useCustomLogin";
import { login } from "../../slices/loginSlice";

function KaKaoRedirectPage(props) {
  //
  const [searchParams] = useSearchParams();

  const { moveToPath, saveAsCookie } = useCustomLogin();

  const authCode = searchParams.get("code");

  //const dispatch = useDispatch();

  // 여기서 카카오 엑세스 토큰을 받을 때 useEffect를 사용하는 이유가 뭘까?
  useEffect(() => {
    console.log("[DEBUG CHECK] useEffect 실행됨, Received authCode:", authCode); // 이 로그가 찍히는지 확인

    // if (!authCode) {
    //   console.error("error! authCode가 존재하지 않음!");
    //   return;
    // }

    // getAccessToken(authCode).then((accessToken) => {
    //   // 여긴 문제없는것 같다
    //   console.log(accessToken); // 여기의 data는 카카오에서 전달해주는 accessTokenf
    //   console.log("React 프론트엔드 에서 받은 엑세스 토큰 : ", accessToken);

    //   getMemberWithAccessToken(accessToken).then((result) => {
    //     console.log("----------------------------");
    //     console.log(result); // 로그인 하면 나오는 결과값
    //   });
    // });
    // getAccessToken(authCode)
    //   .then((accessToken) => {
    //     if (!accessToken) {
    //       console.error("[ERROR] accessToken을 가져오지 못함");
    //       return;
    //     }

    //     return getMemberWithAccessToken(accessToken);
    //   })
    //   .then((result) => {
    //     console.log("[DEBUG] 최종 result:", result);
    //   })
    //   .catch((error) => {
    //     console.error("[ERROR] 전체 로그인 과정에서 오류 발생:", error);
    //   });
    getAccessToken(authCode).then((accessToken) => {
      getMemberWithAccessToken(accessToken).then((memberInfo) => {
        console.log("------------------");
        console.log(memberInfo);

        saveAsCookie(memberInfo);

        //dispatch(login(memberInfo)); // 이 부분을 인식 못하고 있다는데 왜지

        if (memberInfo && memberInfo.social) {
          // 소셜 로그인 유저라면
          moveToPath("/member/modify");
        } else {
          moveToPath("/");
        }
      });
    });
  }, [authCode]);

  return (
    <div>
      <div>Kakao Login Redirect</div>
      <div>{authCode}</div>
    </div>
  );
}

export default KaKaoRedirectPage;
