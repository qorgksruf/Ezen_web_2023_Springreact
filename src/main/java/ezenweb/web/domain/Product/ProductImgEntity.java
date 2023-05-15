package ezenweb.web.domain.Product;


import ezenweb.web.domain.File.FileDto;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="productimg")
public class ProductImgEntity {

   @Id
   private String uuidFile;     //1.이미지식별이름 [오토키]

   @Column
   private String originalFilename;

    //3. 제품객체 [fk]
    @ManyToOne //fk 필드 선언시
    @JoinColumn(name="id") //DB테이블에 표시될 FK 핃드명과 동일해야함
    @ToString.Exclude //순환참조 방지 [양방향일때 필수]
    private ProductEntity productEntity;

    public FileDto toFileDto() {
        return FileDto.builder()
                .uuidFile(this.uuidFile)
                .originalFilename(this.originalFilename)
                .build();
    }


}
