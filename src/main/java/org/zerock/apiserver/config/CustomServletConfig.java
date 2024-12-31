package org.zerock.apiserver.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.zerock.apiserver.controller.formatter.LocalDateFormatter;
import org.zerock.apiserver.dto.TodoDTO;
import org.zerock.apiserver.service.TodoService;

import java.util.Map;

@Configuration // 스프링의 설정 클래스임을 나타냄
@Log4j2
@RequiredArgsConstructor
public class CustomServletConfig implements WebMvcConfigurer {

    private final TodoService todoService;

    @Override
    public void addFormatters(FormatterRegistry registry) {

        log.info("addFormatters");

        registry.addFormatter(new LocalDateFormatter());
    }

    // 포메터 역할
    // 클라이언트로부터 "2023-12-31" 같은 문자열을 받았을 때 이를 LocalDate로 변환하거나,
    // 반대로 변환된 LocalDate를 응답으로 보낼 때 다시 문자열로 변환

    // CORS 는 Config 설정 파일에 하는게 낫다
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .maxAge(500)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                .allowedOrigins("*");
    }

}
