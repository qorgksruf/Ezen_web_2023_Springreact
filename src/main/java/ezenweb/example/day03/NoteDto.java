package ezenweb.example.day03;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//룸북사용
@Data   //@Getter @Setter @toString 전부 제공해줌
@AllArgsConstructor@NoArgsConstructor@Builder
public class NoteDto {
    private int nno;
    private String ncontents;

    //entity 에서 받은 dto룰 다시 엔티티로
    public NoteEntity toEntity(){
        return NoteEntity.builder()
              .nno(nno)
              .ncontents(ncontents)
              .build();
    }


}
