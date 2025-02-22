import React, { useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { getAccessToken, getMemberWithAccessToken } from "../../api/kakaoApi";

function KaKaoRedirectPage(props) {
  const [searchParams] = useSearchParams();

  const authCode = searchParams.get("code");

  // 여기서 카카오 엑세스 토큰을 받을 때 useEffect를 사용하는 이유가 뭘까?
  useEffect(() => {
    getAccessToken(authCode).then((accessToken) => {
      console.log(data); // 여기의 data는 카카오에서 전달해주는 accessToken
      console.log("React 프론트엔드 에서 받은 엑세스 토큰 : ", accessToken);

      getMemberWithAccessToken(accessToken).then((result) => {
        console.log("----------------------------");
        console.log(result); // 로그인 하면 나오는 결과값
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
