package ezenweb.web.domain.board;


import ezenweb.web.domain.BaesTime;
import ezenweb.web.domain.member.MemberEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reply")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyEntity extends BaesTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rno;
    @Column
    private String rcontent;

    @Column
    private int rindex;

    //게시물 fk
    @ManyToOne
    @JoinColumn(name = "bno") @ToString.Exclude
    private BoardEntity boardEntity;
    //작성자fk
    @ManyToOne
    @JoinColumn(name = "mno")@ToString.Exclude
    private MemberEntity memberEntity;

    //출력용
    public ReplyDto toDto(){
        return ReplyDto.builder()
                .rno(this.rno)
                .rcontent(this.rcontent)
                .rindex(this.rindex)
                .rdate(this.cdate.toLocalDate().toString())
                .build();

    }




}