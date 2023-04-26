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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.lang.reflect.Member;
import java.util.*;

@Slf4j
@Service //서비스 레이어
public class MemberService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Override// 토큰 결과[JSON]{필드명:값,필드명:값}
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        log.info("서비스정보::" + oAuth2UserService.loadUser(userRequest));

        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        log.info("회원정보::" + oAuth2User.getAuthorities());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String email = null;
        String name = null;

        if (registrationId.equals("kakao")) { //카카오 회원이면

            Map<String, Object> kakao_account = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            log.info("카카오 회원 정보 : " + kakao_account);
            Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");
            log.info("카카오 프로필 정보 : " + profile);

            email = (String) kakao_account.get("email");
            log.info("카카오 이메일 : " + email);
            name = (String) profile.get("nickname");
            log.info("카카오 이름 : " + name);

        } else if (registrationId.equals("naver")) { //만약에 네이버 회원이면

            Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
            email = (String) response.get("email");
            name = (String) response.get("nickname");

        } else if (registrationId.equals("google")) {  //만약에 구글 회원이면
            //구글의 이메일 호출
            email = (String) oAuth2User.getAttributes().get("email");
            //log.info("google name:"+registrationId);
            //구글의 이름 호출
            name = (String) oAuth2User.getAttributes().get("name");
        }


        MemberDto memberDto = new MemberDto();

        String oauth2UserInfo = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        log.info("oauth2UserInfo::" + oauth2UserInfo);

        //log.info("google email:"+registrationId);
        memberDto.setMemail(email);
        memberDto.setMname(name);
        Set<GrantedAuthority> 권한목록 = new HashSet<>();
        SimpleGrantedAuthority 권한 = new SimpleGrantedAuthority("ROLE_user");
        권한목록.add(권한);
        memberDto.set권한목록(권한목록);

        //1.db에 저장하기전에 해당 이메일로 된 이메일 존재하는 지 검사
        MemberEntity entity = memberEntityRepository.findByMemail(email);
        if (entity == null) {//첫방문
            //DB 처리[oauth2회원을 db에 회원가입 근데 첫 방문시에만 db처리해야함]
            memberDto.setMrole("oauthuser");//db에 저장할 권한명
            memberEntityRepository.save(memberDto.toEntity());
        } else {//두번쨰 방문 이상 수정 처리
            entity.setMname(name);
        }
        memberDto.setMno(entity.getMno()); //위에 생성된 혹은 검색된 엔티티의 회원번호
        return memberDto;
    }

    @Autowired
    private MemberEntityRepository memberEntityRepository;
    @Autowired
    private HttpServletRequest request; //요청 객체

    //1. 일반 회원가입[본 어플리케이션에서 가입한 사람]
    @Transactional
    public boolean write(MemberDto memberDto) {

        //중복검사
        MemberEntity entity2= memberEntityRepository.findByMemail(memberDto.getMemail());

        if(entity2 != null && memberEntityRepository.existsByMphone(memberDto.getMphone())==true){
            return false;
        }

        //스프링 시큐리티에서 제공하는 암호화[사람이 이해하기 어렵고 컴퓨터는 이해할 수 있는 단어] 사용하기
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //인코더 : 암호화  즉 형식을로 변경 //디코더: 원본으로 되돌리기
        log.info("비크립트암호화 사용" + passwordEncoder.encode("1234"));
        memberDto.setMpassword(passwordEncoder.encode(memberDto.getMpassword()));

        //등급부여
        memberDto.setMrole(("user"));

        MemberEntity entity =
                memberEntityRepository.save(memberDto.toEntity());
        if (entity.getMno() > 0) {
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


    //쌤이랑 한거 아이디 중복검사
    public boolean idcheck(String memail){

        return  memberEntityRepository.existsByMemail(memail);


    }

    //과제
    //전화번호중복
    public boolean phonecheck(String mphone){

        return memberEntityRepository.existsByMphone(mphone);

    }


    //회원수정
    @Transactional
    public boolean update(MemberDto memberDto) {
        Optional<MemberEntity> entityOptional =
                memberEntityRepository.findById(memberDto.getMno());
        if (entityOptional.isPresent()) {
            MemberEntity entity = entityOptional.get();
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
    public boolean delete(int mno, String mpassword) {
        Optional<MemberEntity> entityOptional =
                memberEntityRepository.findById(mno);
        if (entityOptional.isPresent()) {
            MemberEntity entity = entityOptional.get();
            if (new BCryptPasswordEncoder().matches(mpassword, entity.getMpassword())) {
                memberEntityRepository.delete(entity);
                return true;
            }

        }
        return false;
    }

    //[스프링 시큐리티 적용했을때 사용되는 로그인 메소드]
    @Override
    public UserDetails loadUserByUsername(String memail) throws UsernameNotFoundException {
        //1.UserDetailsService 인터페이스 구현
        //2.loadUserByUsername() 메소드: 아이디 검증
        //패스워드 검증 [시큐리티 자동]
        MemberEntity entity = memberEntityRepository.findByMemail(memail);
        if (entity == null) {
            throw new UsernameNotFoundException("해당 계정의 회원이 없습니다.");
        }
        //3.검증 후 세션에 저장할 DTO 반환
        MemberDto dto = entity.toDto();
        //Dto 권한 넣어주기

        //권한 목록 만들기
        Set<GrantedAuthority> 권한목록 = new HashSet<>();
        //권한객체만들기 [db에존재하는 권한명으로(ROLE_!!!)으로]
        //권한 없을경우:콘솔에 ROLE_ANONYMOUS / 권한있을경우 ROLE_권한명, ROLR_admin, role_user
        SimpleGrantedAuthority 권한명1 = new SimpleGrantedAuthority("ROLE_" + entity.getMrole());

        //만든 권한 객체를 권한목록[컬랙션]에 추가
        권한목록.add(권한명1);
        //userdetails에 권한목록 대입
        dto.set권한목록(권한목록);
        // Collection<? extends GrantedAuthority>authorities;


        log.info("dto반환한거 들어오는지:::::" + dto);
        return dto; //UserDetails : 원본데이터의 검증할 계정,패스워드 포함
    }

    //[세션에 존재하는]회원정보
    @Transactional
    public MemberDto info() {
        log.info("Auth:" + SecurityContextHolder.getContext().getAuthentication());
        log.info("Auth:" + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (o.equals("anonymousUser")) {
            return null;
            //인증 실패시: anonymousUser
            //인증 성공시: 회원정보[Dto]
        }
        return (MemberDto)o;
    }

    //회원아이디찾기 [과제]
    @Transactional
    public String findid(MemberDto memberDto) {
        Optional<MemberEntity> Optionalentity = memberEntityRepository.findByMnameAndMphone(memberDto.getMname(), memberDto.getMphone());
        if (Optionalentity.isPresent()) {
            MemberEntity entity = Optionalentity.get();
            String memail = entity.toDto().getMemail();

            return memail;
        }
        return null;
    }

    //비밀번호수정
    @Transactional
    public int findpassword(MemberDto memberDto) {
        Optional<MemberEntity> Optionalentity = memberEntityRepository.findByMemailAndMphone(memberDto.getMemail(), memberDto.getMphone());
        if (Optionalentity.isPresent()) {

            MemberEntity entity = Optionalentity.get();
            Random random = new Random();
            int mno = random.nextInt(9);
            //int no=Integer.parseInt(entity.setMpassword(memberDto.getMpassword(mno)));
            //스프링 시큐리티에서 제공하는 암호화[사람이 이해하기 어렵고 컴퓨터는 이해할 수 있는 단어] 사용하기
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //인코더 : 암호화  즉 형식을로 변경 //디코더: 원본으로 되돌리기
            log.info("비크립트암호화 사용" + passwordEncoder.encode(mno+""));

            entity.setMpassword( passwordEncoder.encode(mno+"") );
            return mno;

        }
        return 0;
    }

    //회원수정과제
    @Transactional
    public boolean memberupdate(MemberDto memberDto) {
        Optional<MemberEntity> entityOptional =
                memberEntityRepository.findById(memberDto.getMno());
        if (entityOptional.isPresent()) {
            MemberEntity entity = entityOptional.get();
            entity.setMname(memberDto.getMname());
            entity.setMphone(memberDto.getMphone());
            return true;
        }
        return false;
    }
}

    //1.db에 저장하기전에 해당 이메일로 된 이메일 존재하는 지 검사




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


