package org.zerock.apiserver.service;

import org.springframework.transaction.annotation.Transactional;
import org.zerock.apiserver.dto.MemberDTO;

@Transactional
public interface MemberService {

    MemberDTO getKakaoMember(String accessToken); // 카카오 로그인을 위한 메소드
}
