package ezenweb.web.domain.Todo;


import ezenweb.web.domain.BaesTime;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "todo")
public class TodoEntity extends BaesTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String title;
    @Column
    private boolean done; //체크여부

    //출력용
    public TodoDto todoDto(){
        return TodoDto.builder()
                .id(this.id)
                .title(this.title)
                .done(this.done)
                .build();
    }

}
