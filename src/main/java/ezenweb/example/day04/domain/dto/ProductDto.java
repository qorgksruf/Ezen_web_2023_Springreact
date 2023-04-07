package ezenweb.example.day04.domain.dto;

import ezenweb.example.day04.domain.entity.product.ProductEntity;
import lombok.*;

@Data   //getter setter tostring
@AllArgsConstructor@NoArgsConstructor //풀 빈생성자
@Builder  //빌더패턴 ->생성자의 기본 제약조건을 무시하고 개발자감 만들고싶어하는 생성자 만들어줌
public class ProductDto {
    private int pno;
    private String pname;
    private String pcontent;
    public ProductEntity toEntity(){
        return ProductEntity.builder()
                .pno(this.pno)
                .pname(this.pname)
                .pcontent(this.pcontent)
                .build();

    }


}
