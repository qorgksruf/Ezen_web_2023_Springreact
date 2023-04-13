package ezenweb.web.controller;

import ezenweb.web.domain.member.MemberDto;
import ezenweb.web.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j //로그 기능
@RequestMapping("/member")
public class MemberController {
    //회원가입
    @GetMapping("/signup")
    public Resource getSignup(){
        return new ClassPathResource("templates/member/signup.html");
    }
    //로그인
    @GetMapping("/login")
    public Resource getLogin(){
        return new ClassPathResource("templates/member/login.html");
    }


    //@Autowired없을때 객체[빈]생성
   // MemberService memberService = new MemberService();


    //@Autowired있을떄 객체[빈]자동생성
    @Autowired
    MemberService memberService;

    //회원가입
    @PostMapping("/info")   //URL같아도 HTTTP메소드 다르므로 식별가능
    public boolean write(@RequestBody MemberDto memberDto){ //java클래스내 메소드 이름은 중복 불가능
        log.info("member info info"+memberDto);
        boolean result = memberService.write(memberDto);
        return result;
    }



    //회원정보 호출
    @GetMapping("/info")
    public MemberDto info(){
        MemberDto result=memberService.info();
        return result;
    }
    //회원정보 수정
    @PutMapping("/info")
    public boolean update(@RequestBody MemberDto memberDto) {
        log.info("member info update"+memberDto);
        boolean result = memberService.update(memberDto);
        return result;
    }
    //회원정보 탈퇴
    @DeleteMapping("/info")
    public boolean delete(@RequestParam int mno){
        log.info("member info delete"+mno);
        boolean result = memberService.delete(mno);
        return result;
    }

    //--------------스프링 시큐리티 사용하기 전---------------------//
/*    @PostMapping("/login")
    public boolean login(@RequestBody MemberDto memberDto){
       boolean result = memberService.login(memberDto);
        return result;
    }*/
/*
    //로그아웃
    @GetMapping("/logout")public boolean logout(){
        return memberService.logout();
    }*/

}
