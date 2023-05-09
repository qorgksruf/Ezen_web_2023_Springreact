package ezenweb.web.domain.Product;


import ezenweb.web.domain.BaesTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity@Table  (name="product")
public class ProductEntity extends BaesTime {
    @Id private String id;//제품번호 [jpa는 1개 이상 ID필요]
    @Column(nullable = false) private  String pname;//제품명
    @Column(nullable = false) private int pprice;//제품가격
    @Column(nullable = false) private String pcategory; //제품카테고리
    @Column(nullable = true, columnDefinition = "text") private String pcommnet;//제품설명
    @Column(nullable = false, length = 100) private  String pmanufacturer;//제조사
    @ColumnDefault("0")@Column(nullable = false) private byte pstate;//제품상태 [0: 판매중, 1: 판매중지, 2: 재고없음]
    @ColumnDefault("0")@Column(nullable = false) private int pstock; //제품 재고/수량
    //제품이미지[1:다] 연관관계 [*추후]
    //구매내역[1:다] 연관관게 [*추후]


    //1.출력용 [관리자 페이지에서] 여긴 모든 제품정보 뿌릴 포장지
    public ProductDto toAdminDto(){
        return ProductDto.builder()
                .id(this.id)
                .pname(this.pname)
                .pprice(this.pprice)
                .pcategory(this.pcategory)
                .pcommnet(this.pcommnet)
                .pmanufacturer(this.pmanufacturer)
                .pstate(this.pstate)
                .pstock(this.pstock)
                .cdate(this.cdate.toString())
                .udate(this.udate.toString())
                .build();
    }
   //2. 출력용 [사용자보는 입장- 메인 페이지에서 출력용]
   //public ProductDto toAdminDto(){}
}
