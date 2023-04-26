package ezenweb.web.domain.board;


import ezenweb.web.domain.member.MemberDto;
import ezenweb.web.domain.member.MemberEntity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyDto {

    private int rno;
    private String rcontent;
    private int bno;
    private int mno;


    //엔티티화하기
    public ReplyEntity toReplyEntity(){
        return ReplyEntity.builder()
                .rno(this.rno)
                .rcontent(this.rcontent)
                .build();
    }







}
