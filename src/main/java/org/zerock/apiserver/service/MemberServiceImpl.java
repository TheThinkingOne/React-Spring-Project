package org.zerock.apiserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders; // 이건 맞음
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
import org.zerock.apiserver.dto.MemberModifyDTO;
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
        log.info("[DEBUG] getKakaoMember() 실행됨, accessToken: " + accessToken);

        if (accessToken == null || accessToken.isEmpty()) {
            log.error("[ERROR] accessToken이 null 또는 비어 있음");
            return null;
        }

        // accessToken 을 이용해서 사용자 정보 가져오기

        // 카카오 연동 닉네임 -- 이메일 주소에 해당(예전에는 카카오 로그인하면 이메일 가져오는게 됬지만 지금은 안되니까...)
        String nickname = getEmailFromKakaoAccessToken(accessToken); // 카카오에서 이메일 가져오기
        log.info("가져온 닉네임: " + nickname);
        if (nickname == null) {
            log.error("[ERROR] getEmailFromKakaoAccessToken 에서 닉네임을 가져오지 못함");
            return null;
        }

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

        // 이미 회원이 아닌경우(신규 가입자인 경우)
        Member socialMember = makeSocialMember(nickname);

        memberRepository.save(socialMember); // 소셜 유저 저장

        MemberDTO memberDTO = entityToDTO(socialMember); // 소셜 유저 정보를 DTO 에 담기
        log.info("[DEBUG] 신규 회원 저장됨: " + socialMember);

        return memberDTO;
    }

    // 회원정보 변경을 위한 서비스 IMPL 메소드
    @Override
    public void modifyMember(MemberModifyDTO memberModifyDTO) {
        Optional<Member> result = memberRepository.findById(memberModifyDTO.getEmail());

        Member member = result.orElseThrow();

        // 이 페이지에서 정보를 수정했다는 것은 더이상 소셜 회원이 아니라는 것?
        member.changeNickname(memberModifyDTO.getNickname());
        member.changeSocial(false);
        member.changePw(passwordEncoder.encode(memberModifyDTO.getPw()));

        memberRepository.save(member);
    }

    private Member makeSocialMember(String email) {
        // 원래는 이메일을 가지고 사용자 Entity 를 생성하는데 이메일이 안되니 아이디로 하나
        String tempPassword = makeTempPassword();

        log.info("임시 비번 tempPassword: " + tempPassword);

        Member member = Member.builder()
                .email(email) // 이메일을 닉네임으로 대신할것
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

        log.info("[DEBUG] getEmailFromKakaoAccessToken 실행됨, accessToken: " + accessToken);
        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        // 요청에 필요한 카카오 값을 해더에 추가
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>(headers); // 정보 담는 엔티티

        // 위까진 동일

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

//        ResponseEntity<LinkedHashMap> response = // 카카오에서 받아오는 정보는 LinkedHashMap 이다.
//                restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, entity, LinkedHashMap.class);
//
//        log.info("카카오 응답: " + response);
//
//        // 윗줄의 response 를 LinkedHashMap 으로 끄집어내서 bodyMap 에 담기
//        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();
//
//        // 여기는 교재랑 다른부분
//        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("properties"); // 이전에는 kakao_account 대신 properties 가 적혀있었음
//
//        log.info("--------------check for Kakao LinkedHashMap------------");
//        log.info(bodyMap);
//        log.info("kakaoAccount = " + kakaoAccount);
//
//        String nickname = kakaoAccount.get("nickname"); // 카카오 닉네임
//
//        log.info("카카오 닉네임 = " + nickname);
//
//        return nickname;
        try {
            ResponseEntity<LinkedHashMap> response = restTemplate.exchange(
                    uriComponents.toString(),
                    HttpMethod.GET,
                    entity,
                    LinkedHashMap.class
            );

            log.info("[DEBUG] 카카오 API 응답: " + response.getBody());

            LinkedHashMap<String, Object> bodyMap = response.getBody();
            LinkedHashMap<String, String> kakaoAccount = (LinkedHashMap<String, String>) bodyMap.get("properties");

            if (kakaoAccount == null) {
                log.error("[ERROR] 카카오 API 응답에서 properties 키를 찾을 수 없음");
                return null;
            }

            String nickName = kakaoAccount.get("nickname");
            log.info("[DEBUG] 카카오에서 가져온 닉네임: " + nickName);

            return nickName;
        } catch (Exception e) {
            log.error("[ERROR] 카카오 API 요청 실패:", e);
            return null;
        }
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
