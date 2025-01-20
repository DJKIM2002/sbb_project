package com.mysite.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
// 이 파일이 환경 설정 파일임을 의미
@EnableWebSecurity
// 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만듦
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	@Bean
	// 해당 메서드를 빈 객체로 등록
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
					.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
		// 인증되지 않은 모든 페이지의 요청을 허용
		// H2 콘솔의 경우 스프링 프레임워크가 아니므로 CSRF 토큰을 발행하는 기능이 없어 403 에러 발생
		// CSRF(Cross Site Request Forgery, 사이트 간 요청 위조)
		// 웹 보안 취약점의 일종으로, 사용자가 자신의 의미와는 무관하게 공격자가 의도한 행위를 특정 웹 사이트에 요청하게 하는 공격
		// 공격자는 사용자의 계정에 대한 완전한 제어권을 얻을 수도 있음
		// 사용자 입장에서는 이상항 URL을 함부로 클릭하지 않고, 의심 메일을 열어보지 않는 것이 중요
		// CSRF 방어 방법 : 1. Referrer check(리퍼러 체크), 2. CAPTCHA 도입, 3. CSRF 토큰 사용
		// 1. Http 요청 헤더 정보에서 Referrer 정보를 확인, 보통 host와 Referrer 값이 일치하므로 둘을 비교(이것만으로도 많은 수의 공격을 방어할 수 있음)
		// 3. 사용자 세션에 임의에 값을 저장하여 모든 요청마다 해당 값을 포함하여 전송하도록 하는 방법, 서버에서 요청을 받을 때마다, 세션에 저장된 값과 요청으로 전송된 값이 일치하여 검증/방어하는 방법
		.formLogin((formLogin) -> formLogin // 로그인
				.loginPage("/user/login") // 로그인 페이지 url 설정
				.defaultSuccessUrl("/")) // 로그인 성공 시 이동할 url 설정
		.logout((logout) -> logout // 로그아웃
				.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) // 로그아웃 url 설정
				.logoutSuccessUrl("/") // 로그아웃에 성공하면 이동될 url 설정
				.invalidateHttpSession(true)) // 로그아웃 시 사용자 세션을 삭제
		;
		return http.build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
