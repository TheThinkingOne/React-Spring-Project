package org.zerock.apiserver.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.zerock.apiserver.security.filter.JWTCheckFilter;
import org.zerock.apiserver.security.handler.APILoginFailHandler;
import org.zerock.apiserver.security.handler.APILoginSuccessHandler;
import org.zerock.apiserver.security.handler.CustomAccessDeniedHandler;

import java.util.Arrays;
import java.util.List;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {

    // 세션 비활성화 하는 이유
    // https://chatgpt.com/share/67a058ff-a464-800f-8134-961ab988fd47

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        log.info("----------------- Spring Security Config ---------------");

        // CORS 설정
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        // 폼 로그인 비활성화
        http.formLogin(config -> config.disable());

        // 세션 안 만들기 api 서버는 기본적으로 무상태이기 때문이라고 한다.
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> {
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.NEVER);
        });

        // CSRF 비활성화
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        // 🔹 인증 예외 처리 추가
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/products/view/**").permitAll()  // 이미지 요청은 인증 없이 허용 이거 추가함
                .anyRequest().authenticated() // 나머지 요청은 인증 필요
        );

        // 로그인 설정
        http.formLogin(config -> {
            config.loginPage("/api/member/login");
            config.successHandler(new APILoginSuccessHandler());
            config.failureHandler(new APILoginFailHandler());
        });

        // 🔹 JWT 필터 등록
        http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        // 🔹 권한 부족 예외 처리
        http.exceptionHandling(config -> {
            config.accessDeniedHandler(new CustomAccessDeniedHandler()); // 403 발생시 핸들링?
        });

        return http.build();
    }


    // 패스워드 인코더
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // 모든 도메인 허용
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization")); // <- 추가
        configuration.setAllowCredentials(true); // 인증된 요청 허용

        // 🔹 브라우저가 이미지 요청을 CORS로 차단하지 않도록 허용
        configuration.addExposedHeader("Content-Type");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


}
