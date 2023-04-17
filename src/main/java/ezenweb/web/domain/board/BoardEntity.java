package ezenweb.web.domain.board;

import ezenweb.web.domain.member.MemberEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "board")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardEntity {
    @Id //primary key //jpa 사용시 한개 이상 필수
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int bno;

    @Column
    private String btitle;

    @Column
    private String bcontent;
    //FK = 외래키
    //카테고리번호
    @ManyToOne //다수가 하나에게 [fk--->pk]
    @JoinColumn(name = "cno") //fk필드명
    @ToString.Exclude //해당 필드는 @ToString 제외하는 필드 [*양방향 필수]
    private CategoryEntity categoryEntity;

    //회원번호[작성자]
    @ManyToOne //다수가 하나에게 [fk--->pk]
    @JoinColumn(name = "mno")
    @ToString.Exclude
    private MemberEntity memberEntity;

    //댓글ㄹ목록
    @OneToMany(mappedBy = "boardEntity")
    @Builder.Default
    private List<ReplyEntity> replyEntityList = new ArrayList<>();

}
