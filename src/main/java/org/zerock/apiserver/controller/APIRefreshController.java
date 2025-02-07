package org.zerock.apiserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.apiserver.util.CustomJWTException;
import org.zerock.apiserver.util.JWTUtil;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class APIRefreshController {

    // 안전하게 하려면 PostMapping 만 하는게 좋다함
    @RequestMapping("/api/member/refresh")
    public Map<String, Object> refresh(
            @RequestHeader("Authorization") String authHeader,
            String refreshToken
    ) {

        if(refreshToken == null) {
            throw new CustomJWTException("NULL_REFRASH");
        }
        if(authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID_STRING");
        }

        // 엑세스 토큰 만료 여부 따져야 함
        String accessToken = authHeader.substring(7); // Bearer ~
        if(!checkExpiredToken(accessToken)) { // 만료된게 아니라면
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        // Refresh 토큰 검증
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);
        log.info("refresh ... claims: " + claims);

        String newAccessToken = JWTUtil.generateToken(claims, 10); // 새로운 엑세스 토큰 생성?

        // Refresh 토큰의 남은 유효시간 계산해서 적으면 다시 갱신
        String newRefreshToken = checkTime((Integer) claims.get("exp")) ?
                JWTUtil.generateToken(claims, 60*24) : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);

    }

    //시간이 1시간 미만으로 남았다면
    private boolean checkTime(Integer exp) {
        //JWT exp를 날짜로 변환
        java.util.Date expDate = new java.util.Date( (long)exp * (1000 ));
        //현재 시간과의 차이 계산 - 밀리세컨즈
        long gap = expDate.getTime() - System.currentTimeMillis();
        //분단위 계산
        long leftMin = gap / (1000 * 60);
        //1시간도 안남았는지..
        return leftMin < 60;
    }

    // 토큰이 만료되었는지 확인
    private boolean checkExpiredToken(String token) {
        try{
            JWTUtil.validateToken(token); //
        }catch(CustomJWTException ex) {
            if(ex.getMessage().equals("Expired")){
                return true;
            }
        }
        return false;
    }

}
