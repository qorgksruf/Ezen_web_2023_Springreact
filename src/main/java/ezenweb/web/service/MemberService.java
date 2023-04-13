package ezenweb.web.service;

import ezenweb.web.domain.member.MemberDto;
import ezenweb.web.domain.member.MemberEntity;
import ezenweb.web.domain.member.MemberEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.lang.reflect.Member;
import java.util.*;

@Slf4j
@Service //서비스 레이어
public class MemberService implements UserDetailsService {


    @Autowired
    private MemberEntityRepository memberEntityRepository;
    @Autowired
    private HttpServletRequest request; //요청 객체
    //1. 일반 회원가입[본 어플리케이션에서 가입한 사람]
    @Transactional
    public boolean write(MemberDto memberDto){
            //스프링 시큐리티에서 제공하는 암호화[사람이 이해하기 어렵고 컴퓨터는 이해할 수 있는 단어] 사용하기
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                //인코더 : 암호화  즉 형식을로 변경 //디코더: 원본으로 되돌리기
        log.info("비크립트암호화 사용"+passwordEncoder.encode("1234"));
        memberDto.setMpassword(passwordEncoder.encode(memberDto.getMpassword()));

        //등급부여
        memberDto.setMrole(("user"));

        MemberEntity entity=
                memberEntityRepository.save(memberDto.toEntity());
            if(entity.getMno()>0){
                return true;
            }
        return false;
    }
    //로그인[시큐리티 사용 했을때]
         //시큐리티는 API[누군가 미리 만들어진 메소드 안에서 커스터마이징[수정]]


/*
    //2.로그인 [시큐리티 사용하기 전]
    @Transactional
    public boolean login(MemberDto memberDto){
*/
/*        boolean result=
                memberEntityRepository.existsByMemailAndMpassword(memberDto.getMemail(),memberDto.getMpassword());
        if(result == true){
            request.getSession().setAttribute("login",memberDto.getMemail());
            return true;
        }
              return false;*//*

        MemberEntity entity=memberEntityRepository.findByMemail(memberDto.getMemail());
        //입력받은 이메일로 엔티티 찾기
        //MemberEntity entity=memberEntityRepository.findByMemailAndMpassword(memberDto.getMemail(),memberDto.getMpassword() );
            log.info("entity 로그인 확인 : "+entity);

        //패스워드 검증
        if( new BCryptPasswordEncoder().matches( memberDto.getMpassword() , entity.getMpassword() ) ){
            // == : 스택 메모리 내 데이터 비교
            // .equals() : 힙 메모리 내 데이터 비교
            // .matches() : 문자열 주어진 패턴 포함 동일여부 체크
            // 세션 사용 : 메소드 밖 필드에 @Autowired HttpServletRequest request 추가
            request.getSession().setAttribute("login" , memberDto.getMemail() ) ;

            return true;
        }

          return false;

    }
*/

    //위의 1번으로 사용해도됨
/*        Optional<MemberEntity>result=memberEntityRepository.findByMemailAndMpassword(memberDto.getMemail(),memberDto.getMpassword());
        log.info("entity 로그인 Optional있는거로 확인 : "+result);

        if(result.isPresent()){
            request.getSession().setAttribute("login",result.get().getMno());
            return true;
        }
                //== 스택 메모리 내 데이터 비교
                //.equals 힙 메모리 비교
                //matches():문자열 주어진 패턴 포함 동일여부 체크
          return true;*/


    //회원수정
    @Transactional
    public boolean update(MemberDto memberDto) {
        Optional<MemberEntity> entityOptional=
                memberEntityRepository.findById(memberDto.getMno());
        if(entityOptional.isPresent()){
            MemberEntity entity= entityOptional.get();
            entity.setMname(memberDto.getMname());
            entity.setMphone(memberDto.getMphone());
            entity.setMrole(memberDto.getMrole());
            entity.setMpassword(memberDto.getMpassword());
            return true;
        }
        return false;
    }
    //회원탈퇴
    @Transactional
    public boolean delete( int mno){
        Optional<MemberEntity>entityOptional=
                memberEntityRepository.findById(mno);
        if(entityOptional.isPresent()){
            memberEntityRepository.delete(entityOptional.get());
        }
        return false;
    }

    //[스프링 시큐리티 적용했을때 사용되는 로그인 메소드]
    @Override
    public UserDetails loadUserByUsername(String memail) throws UsernameNotFoundException {
        //1.UserDetailsService 인터페이스 구현
        //2.loadUserByUsername() 메소드: 아이디 검증
            //패스워드 검증 [시큐리티 자동]
        MemberEntity entity= memberEntityRepository.findByMemail(memail);
        if(entity==null){
            return null;
        }
        //3.검증 후 세션에 저장할 DTO 반환
        MemberDto dto = entity.toDto();
        //Dto 권한 넣어주기

        //권한 목록 만들기
        Set<GrantedAuthority> 권한목록 = new HashSet<>();
        //권한객체만들기 [db에존재하는 권한명으로(ROLE_!!!)으로]
            //권한 없을경우:콘솔에 ROLE_ANONYMOUS / 권한있을경우 ROLE_권한명, ROLR_admin, role_user
        SimpleGrantedAuthority 권한명1 = new SimpleGrantedAuthority("ROLE_"+entity.getMrole());

        //만든 권한 객체를 권한목록[컬랙션]에 추가
        권한목록.add(권한명1);
        //userdetails에 권한목록 대입
        dto.set권한목록(권한목록);
       // Collection<? extends GrantedAuthority>authorities;


        log.info("dto반환한거 들어오는지:::::"+dto);
        return dto; //UserDetails : 원본데이터의 검증할 계정,패스워드 포함
    }

    //[세션에 존재하는]회원정보
    @Transactional
    public MemberDto info(){
       log.info("Auth:"+ SecurityContextHolder.getContext().getAuthentication());
       log.info("Auth:"+ SecurityContextHolder.getContext().getAuthentication().getPrincipal() );
       Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       if(o.equals("annonymousUser")){
           return null;
           //인증 실패시: anonymousUser
           //인증 성공시: 회원정보[Dto]
       }
       return (MemberDto)o;
    }


/*

    //회원정보-
    @Transactional
    public MemberDto info(){
        String memail=(String)request.getSession().getAttribute("login");
        if(memail!=null){
            MemberEntity entity=memberEntityRepository.findByMemail(memail);
            return entity.toDto();
        }
        return null;
*/
/*        Optional<MemberEntity> entityOptional=
                memberEntityRepository.findById(mno);
        if(entityOptional.isPresent()){
            MemberEntity entity= entityOptional.get();
            return entity.toDto();
        }
        return null;*//*

    }
    //2. 로그아웃
    @Transactional
    public boolean logout(){
        request.getSession().setAttribute("login", null);
        return true;
    }
*/

}
