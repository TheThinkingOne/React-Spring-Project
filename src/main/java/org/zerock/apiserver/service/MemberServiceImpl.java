package org.zerock.apiserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.zerock.apiserver.domain.Member;
import org.zerock.apiserver.domain.MemberRole;
import org.zerock.apiserver.dto.MemberDTO;
import org.zerock.apiserver.repository.MemberRepository;

import java.util.LinkedHashMap;
import java.util.Optional;


//import java.net.http.HttpHeaders;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    // 새로운 사용자일 경우 패스워드를 인코딩 해서 DB에 넣어줘야 함
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberDTO getKakaoMember(String accessToken) {
        log.info("getKakaoMember() 실행...");

        // accessToken 을 이용해서 사용자 정보 가져오기

        // 카카오 연동 닉네임 -- 이메일 주소에 해당(예전에는 카카오 로그인하면 이메일 가져오는게 됬지만 지금은 안되니까...)
        String nickname = getEmailFromKakaoAccessToken(accessToken); // 카카오에서 이메일 가져오기
        log.info("가져온 닉네임: " + nickname);

        // 기존 DB에 이미 있는 경우는? 없는 경우는? => 따로 처리해야함
        // 닉네임으로 아이디 찾아보기
        Optional<Member> result = memberRepository.findById(nickname);
        if(result.isPresent()) {

            MemberDTO memberDTO = entityToDTO(result.get());

            log.info("is already existed.............." + memberDTO);

            return memberDTO;

            // 내가 엑세스 토큰을 발행받았다는 이야기는 엑세스 토큰을 발행받았다는 소리이므로
            // 해당 사용자의 DB 정보 유무만 체크하면 됨
            // return

            // 2025/02/20 현재 문제점
            // 소셜 로그인 해도 화면에 로그인 관련 정보 안 나오고 DB에 사용자 정보가 저장되고 있지 않음
        }

        Member socialMember = makeSocialMember(nickname);

        memberRepository.save(socialMember); // 소셜 유저 저장

        MemberDTO memberDTO = entityToDTO(socialMember); // 소셜 유저 정보를 DTO 에 담기

        return memberDTO;
    }

    private Member makeSocialMember(String email) {
        // 원래는 이메일을 가지고 사용자 Entity 를 생성하는데 이메일이 안되니 아이디로 하나
        String tempPassword = makeTempPassword();

        log.info("임시 비번 tempPassword: " + tempPassword);

        Member member = Member.builder()
                .email(email)
                .pw(passwordEncoder.encode(tempPassword))
                .nickname("Social Login Member")
                .social(true)
                .build();

        // 소셜로 로그인한 사람은 닉네임 같은거 나중에 수정할 수 있게 해야함
        member.addRole(MemberRole.USER);

        return member;
    }

    // 사용쟈 정보 가져오는건 restTemplate 로 처리
    private String getEmailFromKakaoAccessToken(String accessToken) {

        String kakaoGetUserURl = "https://kapi.kakao.com/v2/users/me";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        // 요청에 필요한 카카오 값을 해더에 추가
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>(headers); // 정보 담는 엔티티

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURl).build();

        ResponseEntity<LinkedHashMap> response = // 카카오에서 받아오는 정보는 LinkedHashMap 이다.
                restTemplate.exchange(uriBuilder.toString(), HttpMethod.GET, entity, LinkedHashMap.class);

        log.info("카카오 응답: " + response);

        // 윗줄의 response 를 LinkedHashMap 으로 끄집어내서 bodyMap 에 담기
        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

        // 여기는 교재랑 다른부분
        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("properties");

        log.info("--------------check for Kakao LinkedHashMap------------");
        log.info(bodyMap);
        log.info("kakaoAccount = " + kakaoAccount);

        String nickName = kakaoAccount.get("nickname");

        log.info("카카오 닉네임 = " + nickName);

        return nickName;

    }

    // 10자리의 랜덤한 비밀번호 만드는 메소드
    private String makeTempPassword() {

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < 10; i++) {
            buffer.append((char) ((int) (Math.random() * 55) + 65));
        }

        return buffer.toString();
    }

}
