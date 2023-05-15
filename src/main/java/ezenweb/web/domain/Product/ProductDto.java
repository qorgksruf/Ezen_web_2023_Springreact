package ezenweb.web.domain.Product;

import ezenweb.web.domain.File.FileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

     private String id;
     private  String pname;
     private int pprice;
     private String pcategory;
     private String pcommnet;
     private  String pmanufacturer;
     private byte pstate;
     private int pstock;

     //관리자용
     private String cdate;
     private String udate;
     //첨부파일 입력용
     private List<MultipartFile> pimgs;
     private List<FileDto> files = new ArrayList<>();

     //저장용[관리자 페이지]
     public ProductEntity toSaveEntity(){
          return ProductEntity.builder()
                  .id(this.id)
                  .pname(this.pname)
                  .pprice(this.pprice)
                  .pcategory(this.pcategory)
                  .pcommnet(this.pcommnet)
                  .pmanufacturer(this.pmanufacturer)
                  .pstate(this.pstate)
                  .pstock(this.pstock)
                  .build();
     }
}
