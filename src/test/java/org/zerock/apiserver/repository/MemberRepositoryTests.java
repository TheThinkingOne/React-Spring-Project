package org.zerock.apiserver.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.apiserver.domain.Member;
import org.zerock.apiserver.domain.MemberRole;

@SpringBootTest
// 회원가입 강제 추가 테스트
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testInsertMember() {

        for (int i=0; i<10; i++) {

            Member member = Member.builder()
                    .email("user"+i+"@aaa.com")
                    .pw(passwordEncoder.encode("1111"))
                    .nickname("USER"+i)
                    .build();

            member.addRole(MemberRole.USER);
            if(i>=5) {
                member.addRole(MemberRole.MANAGER);
            }
            if(i>=8) {
                member.addRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);

        } // end of for
    }

    @Test
    public void testRead() {

        String email = "user9@aaa.com";

        Member member = memberRepository.getWithRoles(email);

        log.info("-----------------");
        log.info(member);
        log.info(member.getMemberRoleList());
    }

//    SELECT
//    m.email,
//    mmrl.member_email,
//    mmrl.member_role_list,
//    m.nickname,
//    m.pw,
//    m.social
//            FROM
//    apidb.`member` m
//    LEFT JOIN
//    apidb.member_member_role_list mmrl
//    ON m.email = mmrl.member_email
//            WHERE
//    m.email = "user9@aaa.com";

}
