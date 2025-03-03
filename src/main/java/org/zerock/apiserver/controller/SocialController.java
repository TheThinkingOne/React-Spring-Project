package org.zerock.apiserver.controller;

// 소셜 로그인을 위한 컨트롤러

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.apiserver.dto.MemberDTO;
import org.zerock.apiserver.dto.MemberModifyDTO;
import org.zerock.apiserver.service.MemberService;
import org.zerock.apiserver.util.JWTUtil;

import java.util.Map;
import java.util.Objects;

@RestController
@Log4j2
@RequiredArgsConstructor
public class SocialController {

    private final MemberService memberService;

    @GetMapping("/api/member/kakao")
    public Map<String, Object> getMemberFromKakao(String accessToken) {

        log.info("[DEBUG] React 에서 가져온 Access Token: " + accessToken);

//        MemberDTO memberDTO = memberService.getKakaoMember(accessToken);
//
//        log.info("카카오 사용자 정보 :" + memberDTO);
        if (accessToken == null || accessToken.isEmpty()) {
            log.error("[ERROR] accessToken이 null 또는 비어 있음");
            return null;
        }

        // 리액트에서 가져온 액세스 토큰을 여기로 가져옴
        MemberDTO memberDTO = memberService.getKakaoMember(accessToken);
        Map<String, Object> claims = memberDTO.getClaims();

        String jwtAccessToken = JWTUtil.generateToken(claims, 10);
        String jwtRefreshToken = JWTUtil.generateToken(claims, 60*24);

        claims.put("accessToken", jwtAccessToken);
        claims.put("refreshToken", jwtRefreshToken);

        return claims;

        // return new String[]{"AAA","BBB","CCC"};
    }

    // 회원정보 수정하는 PutMapping 메소드
    @PutMapping("/api/member/modify")
    public Map<String, String> modify(@RequestBody MemberModifyDTO memberModifyDTO) {
        log.info("member modify(회원정보 변경)-----------------------" + memberModifyDTO);

        memberService.modifyMember(memberModifyDTO);

        return Map.of("result", "modified");

    }

}
