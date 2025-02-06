package org.zerock.apiserver.security.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Log4j2
public class APILoginFailHandler implements AuthenticationFailureHandler {
    // 로그인 실패했을 때 핸들러
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        log.info("Login failed--------" + exception);

        Gson gson = new Gson();

        String jsonStr = gson.toJson(Map.of("error", "Login Error!!"));

        response.setContentType("application/json"); // 상태코드 따로 안두면 200임
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jsonStr);
        printWriter.close();

    }
}
