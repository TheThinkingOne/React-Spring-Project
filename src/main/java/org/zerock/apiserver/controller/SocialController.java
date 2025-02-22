package org.zerock.apiserver.controller;

// 소셜 로그인을 위한 컨트롤러

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.apiserver.dto.MemberDTO;
import org.zerock.apiserver.service.MemberService;

@RestController
@Log4j2
@RequiredArgsConstructor
public class SocialController {

    private final MemberService memberService;

    @GetMapping("/api/member/kakao")
    public String[] getMemberFromKakao(String accessToken) {

        log.info("React 에서 가져온 Access Token: " + accessToken);

        MemberDTO memberDTO = memberService.getKakaoMember(accessToken);

        log.info("카카오 사용자 정보 :" + memberDTO);

        // 리액트에서 가져온 액세스 토큰을 여기로 가져옴
        memberService.getKakaoMember(accessToken);

        return new String[]{"AAA","BBB","CCC"};
    }

}
