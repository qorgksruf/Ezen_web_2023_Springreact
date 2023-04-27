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
    private String rdate;
    private int rindex;

    private int bno;
    private int mno;


    //저장용
    public ReplyEntity toReplyEntity(){
        return ReplyEntity.builder()
                .rcontent(this.rcontent)
                .rindex(this.rindex)
                .build();
    }







}
