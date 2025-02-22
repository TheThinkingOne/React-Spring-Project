package org.zerock.apiserver.service;

import org.springframework.transaction.annotation.Transactional;
import org.zerock.apiserver.domain.Member;
import org.zerock.apiserver.dto.MemberDTO;
import org.zerock.apiserver.repository.MemberRepository;

import java.util.stream.Collectors;

@Transactional
public interface MemberService {

    MemberDTO getKakaoMember(String accessToken); // 카카오 로그인을 위한 메소드

    default MemberDTO entityToDTO(Member member) {

        MemberDTO dto = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList()));

        return dto;
    }

}
