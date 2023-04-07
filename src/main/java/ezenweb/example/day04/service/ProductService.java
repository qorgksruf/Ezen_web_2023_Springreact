package ezenweb.example.day04.service;

import ezenweb.example.day03.NoteDto;
import ezenweb.example.day03.NoteEntity;
import ezenweb.example.day04.domain.dto.ProductDto;
import ezenweb.example.day04.domain.entity.product.ProductEntity;
import ezenweb.example.day04.domain.entity.product.ProductEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service //서비스
public class ProductService {

    @Autowired
    private ProductEntityRepository productEntityRepository;


    public boolean write(ProductDto dto){
        ProductEntity  entity = productEntityRepository.save(dto.toEntity());
        if(entity.getPno()>0){
            return true;
        }else{
            return false;
        }
    }


    //수정
    @Transactional
    public boolean update(ProductDto dto){
        Optional<ProductEntity>optionalProductEntity=
        productEntityRepository.findById(dto.getPno());

        if(optionalProductEntity.isPresent()){
            ProductEntity entity=optionalProductEntity.get();
            entity.setPname(dto.getPname());
            entity.setPcontent(dto.getPcontent());
            return true;
        }
        return false;
    }




    //전체출력
    public ArrayList<ProductDto> get(){
        //모든 엔티티 호출
        List<ProductEntity> entityList= productEntityRepository.findAll(); //select 대체
        //모든 엔티티를 형변환
        ArrayList<ProductDto> list = new ArrayList<ProductDto>();

        entityList.forEach(e->{
            list.add(e.toDto());
        });
        return list;
    }


    //삭제
    public boolean itemdelete(int nno) {
        //1. 삭제할 식별번호[pk]
        Optional<ProductEntity> optionalNoteEntity = productEntityRepository.findById(nno); //optional 은 null 이 포함된 포장 클래스 NullPointException 검사가능
        //2.
        if (optionalNoteEntity.isPresent()) { //포장클래스내 엔티티가 들어있으면
            ProductEntity productEntity = optionalNoteEntity.get(); //엔티티 꺼내기
            productEntityRepository.delete(productEntity);
            return true;
        }

        return false;
    }


}//class e
