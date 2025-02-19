package org.zerock.apiserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.zerock.apiserver.dto.MemberDTO;
import org.zerock.apiserver.repository.MemberRepository;

import java.util.LinkedHashMap;


//import java.net.http.HttpHeaders;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public MemberDTO getKakaoMember(String accessToken) {
        // accessToken 을 이용해서 사용자 정보 가져오기

        getEmailFromKakaoAccessToken(accessToken); // 카카오에서 이메일 가져오기

        // 기존 DB에 이미 있는 경우는? 없는 경우는? => 따로 처리해야함

        return null;
    }

    // 사용쟈 정보 가져오는건 restTemplate 로 처리
    private void getEmailFromKakaoAccessToken(String accessToken) {

        String kakaoGetUserURl = "https://kapi.kakao.com/v2/users/me";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        // 요청에 필요한 카카오 값을 해더에 추가
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>(headers); // 정보 담는 엔티티

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURl).build();

        ResponseEntity<LinkedHashMap> response = // 카카오에서 받아오는 정보는 LinkedHashMap 이다.
                restTemplate.exchange(uriBuilder.toUri(), HttpMethod.GET, entity, LinkedHashMap.class);

        log.info(response);

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

        log.info("--------------check for kakako LinkedHashMap------------");
        log.info(bodyMap);

    }

}
