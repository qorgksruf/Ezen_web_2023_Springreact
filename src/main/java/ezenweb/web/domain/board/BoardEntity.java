package ezenweb.web.domain.board;

import ezenweb.web.domain.BaesTime;
import ezenweb.web.domain.member.MemberEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "board")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardEntity extends BaesTime {
    @Id //primary key //jpa 사용시 한개 이상 필수
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int bno; //게시물번호

    @Column(nullable = false)
    private String btitle; //게시물 제목[기본값255]

    @Column(columnDefinition = "longtext")
    private String bcontent;

    @Column
    @ColumnDefault("0")//필드 초기값
    private int bview; //조회수

    //작성일,수정을은 BaesTime 클래스로붜 상속받아 진행

    //FK = 외래키[카테고리번호=cno, 회원번호=mno]


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

    //pk=양방향[해당 댓글 엔티티의  fk필드와 매핑]
    //댓글ㄹ목록
    @OneToMany(mappedBy = "boardEntity")
    @Builder.Default
    private List<ReplyEntity> replyEntityList = new ArrayList<>();

    //출력용 Entity --> Dto
    public BoardDto toDto() {
        return BoardDto.builder()
                .bno(this.bno)
                .btitle(this.btitle)
                .bcontent(this.bcontent)
                .cno(this.getCategoryEntity().getCno())
                .cname(this.getCategoryEntity().getCname())
                .mno(this.getMemberEntity().getMno())
                .memail(this.getMemberEntity().getMname())
                .bview(this.bview)
                //날짜형변환
                //.bdate(this.cdate) //얘는 basetime 상속 받았으니까
                .bdate(
                        this.cdate.toLocalDate().toString().equals(LocalDateTime.now().toLocalDate().toString() )?
                        this.cdate.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) :
                        this.cdate.toLocalDate().format(DateTimeFormatter.ofPattern("yy-MM-dd"))
                       )
                .build();
    }

}
/*
*   cdate[LocalDateTime]
*       1.toLocalDate():날짜만 ㅜ출
*
* */