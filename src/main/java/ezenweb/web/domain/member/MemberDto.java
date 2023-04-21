package ezenweb.web.domain.member;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Getter@Setter@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
//시큐리티[UserDetails]+일반DTO
public class MemberDto implements UserDetails, OAuth2User {


    private int mno;     //회원번호

    private String memail;  //회원아이디[이메일]

    private String mpassword;//회원비밀번호

    private String mname;   //회원이름

    private String mphone;   //회원전화번호

    private String mrole;   //회원등급
    private Set<GrantedAuthority> 권한목록;
    private Map<String,Object> 소셜회원정보;
    //추가
    private String cdate;

    private String udate;

    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .mno(this.mno)
                .memail(this.memail)
                .mpassword(this.mpassword)
                .mname(this.mname)
                .mphone(this.mphone)
                .mrole(this.getMrole())
                .build();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.권한목록;
    }

    @Override//패스워드
    public String getPassword() {
        return this.mpassword;
    }

    @Override//계정반환
    public String getUsername() {
        return this.memail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }




    //------------Oauth2user오버라이드-----------------
    @Override
    public String getName() {
        return this.memail;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.소셜회원정보;
    }


}
