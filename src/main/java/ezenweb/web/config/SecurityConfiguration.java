package ezenweb.web.config;


import ezenweb.web.controller.AuthSussessFailHandler;
import ezenweb.web.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration  //스프링 빈에 등록 [MVC 컴포넌트]
@Slf4j

public class SecurityConfiguration extends WebSecurityConfigurerAdapter{



    //인증[로그인]관련 보안 담당 메소드
    @Autowired
    private MemberService memberService;
    @Autowired
    private AuthSussessFailHandler authSussessFailHandler;


    //인증[로그인]관련 보안 담당 메소드
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(new BCryptPasswordEncoder());
        //.passwordEncoder() 서비스 안에서 로그인 패스워드 검증시 사용된 암호화 객체 대입

    }

    @Override //재정의[설정 바꾸기]
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http); //super: 부모 클래스 호출
        http
                 .formLogin()//
                        .loginPage("/member/login")//로그인 으로 사용될 페이지의 매핑 URL
                        .loginProcessingUrl("/member/login")//로그인을 처리할 매핑 url
                        //.defaultSuccessUrl("/")//로그인 성공했을때 이동할 매핑 url
                       //로그인 ��공시 이동 ����  ��
                        //.failureUrl("/member/login")//로그인  실패시
                        .successHandler(authSussessFailHandler)
                        .failureHandler(authSussessFailHandler)//
                        .usernameParameter("memail")//로그인시 사용될 계정 아이디의 필드명
                        .passwordParameter("mpassword")//로그인시 사용될 계정 패스워드의 필드

                        .and()
                            .logout()
                                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                                .logoutSuccessUrl("/")//로그아웃 성공시 이동 매핑  ㅕ기
                                .invalidateHttpSession(true) //세션 초기화
                .and()
                     .oauth2Login()//소셜 로그인 설정
                    .defaultSuccessUrl("/")//로그인 성공시 이동할 매핑 url
                    //.successHandler(authSussessFailHandler)
                    .userInfoEndpoint()//스프링 시큐리티로 들어올 수 있도록 시큐리티 로그인 엔드포인트[종착]
                    .userService(memberService);

        http.cors(); //cors 정책 사용
        http.csrf().disable(); //csrd사용 해제
    }//configure end

    //스프링 시큐리티에 CORS정책 설정[리액트[3000]가 요청 받기 위해서]
/*    @Bean //빈등록
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); //주소
        corsConfiguration.setAllowedMethods(Arrays.asList("HEAD","GET","POST","PUT","DELETE")); //HTTP매소드
        corsConfiguration.setAllowedHeaders(Arrays.asList("content-Type","Cache-Control","Authorization")); //http설정
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }*/

}//securityConfiguration class end
