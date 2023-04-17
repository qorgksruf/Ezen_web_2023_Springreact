package ezenweb.web.domain.board;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDto {
    private int bno;
    private String btitle;
    private String bcontent;
    private int cno;
    private String cname;


    //Entity   변환 메소드
    public CategoryEntity toCategoryEntity(){
        return CategoryEntity.builder()
                .cname(this.cname)
                .build();
    }

    //toBoardEntity
    public BoardEntity toBoardEntity(){
        return BoardEntity.builder()
                .btitle(this.btitle)
                .bcontent(this.bcontent)
                .build();
    }
}
