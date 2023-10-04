package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity //Spring Security를 활성화하는 어노테이션
public class SecurityConfig {
    // Spring Security를 사용하여 웹 애플리케이션의 인증(Authentication) 및 권한 부여(Authorization) 설정을 정의하고,
    // 로그인 및 로그아웃 처리를 구성
    // MemberService 서비스를 주입받아 사용자 관련 로직을 처리하며, 사용자 역할에 따라 접근 권한을 제한

    @Autowired
    MemberService memberService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{ //Spring Security의 필터 체인을 구성
        http.formLogin() //폼 기반 로그인을 활성화하며, 로그인 페이지, 성공 및 실패 URL, 사용자 이름 파라미터 등을 설정
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                //로그아웃 기능을 활성화하며, 로그아웃 요청 URL과 로그아웃 후 성공 시 리다이렉트할 URL을 설정
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/");

        http.authorizeRequests()//메소드는 URL 패턴에 따른 접근 권한을 설정
                .mvcMatchers("/css/**","js/**","/img/**").permitAll()
                .mvcMatchers("/", "/members/**", "/item/**","/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                //'/admin/**' 패턴은 "ADMIN" 역할을 가진 사용자에게만 허용
                .anyRequest().authenticated();

        http.exceptionHandling() //인증 실패 시 사용자 정의 인증 진입 지점(CustomAuthenticationEntryPoint)을 설정
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
                 //CustomAuthenticationEntryPoint클래스 생성은 p.182
        return http.build(); //SecurityFilterChain을 생성하고 반환
    }

    @Bean //비밀번호를 안전하게 저장하기 위해 사용하는 PasswordEncoder 빈을 등록
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

/*@Configuration
@EnableWebSecurity //SpringSecurityFilterChain이 자동으로 포함
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    } //비밀번호 암호화

}*/
