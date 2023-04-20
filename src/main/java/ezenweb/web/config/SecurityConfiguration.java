package ezenweb.web.config;


import ezenweb.web.controller.AuthSussessFailHandler;
import ezenweb.web.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
                //권한에 따른 HTTP GET 요청 제한
                .authorizeHttpRequests()//인증요청
                .antMatchers(("/member/info/mypage"))//인증시에만 사용할 url
                        .hasRole("user")
                .antMatchers(("/member/byeuser"))//인증시에만 사용할 url
                         .hasRole("user")
                .antMatchers(("/member/memberupdate"))//인증시에만 사용할 url
                        .hasRole("user")
                .antMatchers(("/member/info/mypage"))//인증시에만 사용할 url
                    .hasRole("user")//위 url 패턴에 요청할 수 있는 권한명
                .antMatchers(("/board/**"))//게시판페이지는 회원만 가능
                    .hasRole("user")
                .antMatchers("/admin/**")//localhost:8080/admin 이하 페이지는 모두 제한한다는 뜻ㄱ
                    .hasRole("admin")
                .antMatchers("/**")     //localhost:8080 이하 페이지는 권한 해제
                     .permitAll()//그외 요청들에대한 권한 해제한다는 뜻
                    //토큰(ROLE_user): ROL_ 제외한 권한명 작성//인증 자체가 없을경우 로그인페이지로 자동이동됨
                //.antMatchers("/member/info","/member/login","/member/logout").permitAll()
                .and()
                        .csrf() //사이트 간 요청위조[post,put사용 불가능]
                            //.disabled() 모든 http csrd
                                .ignoringAntMatchers("/member/info")//특정 매핑 url csrf 무시
                                 .ignoringAntMatchers("/member/login")
                                 .ignoringAntMatchers("/member/findid")
                                 .ignoringAntMatchers("/member/findpassword")
                                 .ignoringAntMatchers("/member/byeuser")
                                 .ignoringAntMatchers("/member/bye")
                                 .ignoringAntMatchers("/member/memberupdate")
                                  .ignoringAntMatchers("/board/category/write")
                                 .ignoringAntMatchers("/board/write")
                                 .ignoringAntMatchers("/board/myboards")
                                .ignoringAntMatchers("/board/delete")
                                .ignoringAntMatchers("/todo")

                .and() //기능 추가할때 사용되는 메소드
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
                    //.defaultSuccessUrl("/")//로그인 성공시 이동할 매핑 url
                    .successHandler(authSussessFailHandler)
                    .userInfoEndpoint()//스프링 시큐리티로 들어올 수 있도록 시큐리티 로그인 엔드포인트[종착]
                    .userService(memberService);
    }


}
