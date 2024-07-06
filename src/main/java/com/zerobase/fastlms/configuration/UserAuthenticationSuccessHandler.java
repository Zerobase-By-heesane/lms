package com.zerobase.fastlms.configuration;

import com.zerobase.fastlms.member.entity.LoginHistory;
import com.zerobase.fastlms.member.repository.LoginHistoryRepository;
import com.zerobase.fastlms.util.IPUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final LoginHistoryRepository memberLoginHistoryRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // 로그인 성공 시 실행할 로직
        String userAgent = httpServletRequest.getHeader("User-Agent");
        String userIp = IPUtil.getClientIp(httpServletRequest);
        log.info("userAgent: {}", userAgent);
        log.info("userIp: {}", userIp);

        // 로그인 아이디
        String loginId = authentication.getName();
        log.info("loginId: {}", loginId);

        // 로그인한 시간
        LocalDateTime loginDt = LocalDateTime.now();

        memberLoginHistoryRepository.save(LoginHistory.builder()
                .userId(loginId)
                .loginDt(loginDt)
                .loginIp(userIp)
                .loginAgent(userAgent)
                .build());

        // 예: 특정 페이지로 리다이렉트
        httpServletResponse.sendRedirect("/member/login");
    }
}
