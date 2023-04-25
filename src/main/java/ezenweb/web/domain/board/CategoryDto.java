package ezenweb.web.domain.board;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private int cno;
    private String cname;

}
