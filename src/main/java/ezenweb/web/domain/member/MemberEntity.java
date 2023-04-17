package ezenweb.web.domain.member;

import ezenweb.web.domain.BaesTime;
import ezenweb.web.domain.board.BoardEntity;
import ezenweb.web.domain.board.ReplyEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "member")
public class MemberEntity extends BaesTime {
    @Id //primary key //jpa 사용시 한개 이상 필수
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int mno;     //회원번호

    @Column private String memail;  //회원아이디[이메일]

    @Column private String mname;   //회원이름

    @Column private String mphone;   //회원전화번호

    @Column private String mpassword;   //회원전화번호

    @Column private String mrole;   //회원등급/권한 명

    @OneToMany(mappedBy="memberEntity")
    @Builder.Default
    private List<BoardEntity> boradEntityList= new ArrayList<>();

    //댓글 목록 = 내가 쓴 댓글
    @OneToMany(mappedBy="memberEntity")
    @Builder.Default
    private List<ReplyEntity>replyEntityList= new ArrayList<>();

    //Dto는 출력용
    public MemberDto toDto() {
        return MemberDto.builder()
              .mno(this.mno)
              .mpassword(this.mpassword)
              .memail(this.memail)
              .mname(this.mname)
              .mphone(this.mphone)
              .cdate(this.cdate)
              .udate(this.udate)
              .build();
    }
}
