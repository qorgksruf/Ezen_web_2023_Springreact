package ezenweb.web.domain.Todo;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoDto {
    private int id;
    private String title;
    private boolean done; //체크여부


    //dto=> entity화
    public TodoEntity todoEntity(){
        return TodoEntity.builder()
                .id(this.id)
                .title(this.title)
                .done(this.done)
                .build();

    }


}
