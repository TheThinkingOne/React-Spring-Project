package org.zerock.apiserver.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.apiserver.dto.MemberDTO;
import org.zerock.apiserver.util.JWTUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Log4j2
// JWT 를 필터링 하기위한 필터 클래스
public class JWTCheckFilter extends OncePerRequestFilter {


    @Override // 필터링을 안할 때의 조건을 설정한 메소드
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI(); // 체크할 uri 경로

        log.info("check url--------" + path);

        if (path.startsWith("/api/member/")) {
            // 회원쪽 로그인 할때는 jwt 체크 하지 않겠다(테스트용)
            return true;
        }

        return false; // shouldNOtFilter 에서 false 리턴이면 체크한다는 뜻
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        log.info("----------------------------");
        log.info("----------------------------");
        log.info("----------------------------");

        // jwt 인증헤더
        String authHeader = request.getHeader("Authorization");
        // Bearer //7 JWT 문자열로 구성되어 있음

        try {
            String accessToken = authHeader.substring(7); // 앞 7문자를 떼어낸 것이 엑세스 토큰이다
            Map<String , Object> claims = JWTUtil.validateToken(accessToken); // 유효성 검사

            log.info("JWT claims: " + claims);

            // Security Context Holder 에 사용자 정보 전달
            // 사용자 정보 끄집어내기
            String email = (String) claims.getOrDefault("email", "");
            String pw = (String) claims.getOrDefault("pw", "");
            String nickname = (String) claims.getOrDefault("nickname", "");
            Boolean social = claims.get("social") != null ? (Boolean) claims.get("social") : false;
            List<String> roleNames = claims.get("roleNames") != null ? (List<String>) claims.get("roleNames") : List.of();

            // MemberDTO 생성
            MemberDTO memberDTO = new MemberDTO(email, pw, nickname, social.booleanValue(), roleNames);

            // 로그 출력
            log.info("-----------------------------------");
            log.info(memberDTO);
            log.info(memberDTO.getAuthorities());

            // 인증 토큰 설정
            UsernamePasswordAuthenticationToken authenticationToken = // 스프링 시큐리티가 사용하는 토큰
                    new UsernamePasswordAuthenticationToken(memberDTO, pw, memberDTO.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            // 무상태라서 매번 호출해서 인증한다는 리소스 적인 단점이 있다.

            filterChain.doFilter(request, response); // 필터 등록 필요
        }
        catch (Exception e) {
            log.error("JWT CHECK ERROR ㅠㅅㅠ----------");
            log.error(e.getMessage());

            // 상태코드 여기서 지정할 수 있음
            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR WITH ACCESS TOKEN ㅠㅅㅠ "));

            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();

        }


    }
}
