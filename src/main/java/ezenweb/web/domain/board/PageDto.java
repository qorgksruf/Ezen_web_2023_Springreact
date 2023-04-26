package ezenweb.web.domain.board;


import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDto {
    private long totalCount; //1.전체 게시물수
    private int  totalPage; //2.전체 페이지수
    private List<BoardDto> boardDtoList; //현재 페이지의 게시물 dto들
    private int page;
    private int cno;
    private String key;
    private String keyword;
}
