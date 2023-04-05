package ezenweb.example.day03;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//객체=레코드, 테이블=클래스
@Entity//테이블과 해당 클래스객체간 매핑[연결] //개체, 실제내용물이라는 뜻 Dto 는 걍 이동하는애니까 버려도 되는데 얘는 아님
@Table(name="note")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NoteEntity {
    @Id //primary key //jpa 사용시 한개 이상 필수
    @GeneratedValue(strategy= GenerationType.IDENTITY) //자동완성같은거지 auto 같은거
    private int nno;

    @Column
    private String ncontents;

    //entity-->dto [서비스에서 사용]
    public NoteDto toDto() {
        return NoteDto.builder()
                .nno(this.nno)
                .ncontents(this.ncontents)
                .build();
    }
}
