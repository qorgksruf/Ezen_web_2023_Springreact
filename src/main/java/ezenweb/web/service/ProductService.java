package ezenweb.web.service;


import ezenweb.web.domain.Product.ProductDto;
import ezenweb.web.domain.Product.ProductEntity;
import ezenweb.web.domain.Product.ProductEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    @Autowired private ProductEntityRepository productEntityRepository;
    //1.
    @Transactional
    public List<ProductDto> get(){
        log.info("get");
        //모든 엔티티 호출
       List<ProductEntity>productEntityList= productEntityRepository.findAll();
       //모든 엔티티를 dto로 변환
                //js: 리스트명.forEach ( o=> 실행문);
                //리스트명.map(o=>실행문)
                //java:리스트명.forEach(o-> 실행문)
                //리스트명.stream().map(o-> 실행문).collect(Collectors.to)
        List<ProductDto> productDtoList = productEntityList.stream().map(o-> o.toAdminDto() ).collect(Collectors.toList());

        return productDtoList;

    }
    //2.
    @Transactional
    public boolean post(ProductDto productDto){

        /**/
        log.info("post : " + productDto );

        // 1. id 생성 [ 오늘날짜 + 등록 밀리초 + 난수 ]
        String number = "";

        for( int i=0; i<3; i++ ) {
            number += new Random().nextInt(10);
        }

        String pid = LocalDateTime.now().format( DateTimeFormatter.ofPattern("yyyyMMddSSS") ) + number ;

        // 2. DTO에 id 넣기
        productDto.setId( pid );

        // 3. DB 저장
        productEntityRepository.save( productDto.toSaveEntity() );

        return true;


    }
    //3.
    @Transactional
    public boolean put(ProductDto productDto){
        log.info("put"+productDto);

        Optional<ProductEntity> entityOptional = productEntityRepository.findById(productDto.getId());

       if(entityOptional.isPresent()) {
           ProductEntity entity = entityOptional.get();
            entity.setPcategory( productDto.getPcategory());
            entity.setPcommnet(productDto.getPcommnet());
            entity.setPname(productDto.getPname());
            entity.setPprice(productDto.getPprice());
            entity.setPmanufacturer(productDto.getPmanufacturer());
            entity.setPstock(productDto.getPstock());
            entity.setPstate(productDto.getPstate());
            return true;
        }
        return false;
    }

    //4.
    @Transactional
    public boolean delete(String id){
        log.info("delete"+id);

        Optional<ProductEntity> entityOptional =productEntityRepository.findById(id);

        entityOptional.ifPresent(o->{ productEntityRepository.delete(o); } );

        return false;
    }
}
