package ezenweb.web.service;


import ezenweb.web.domain.File.FileDto;
import ezenweb.web.domain.Product.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.transaction.Transactional;
import java.io.File;
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
    //1. [main 출력용] 현재 판매중인 제품 호출
    @Transactional public  List<ProductDto>mainGet(){
        List<ProductEntity>productEntityList = productEntityRepository.findAllState();
        List<ProductDto>productDtoList = productEntityList.stream().map(
            o -> o.toMinDto()
        ).collect(Collectors.toList());
        return productDtoList;
    }


    //1. [admin 출력용] 모든 제품 호출
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
       ProductEntity productEntity= productEntityRepository.save( productDto.toSaveEntity() );

        //4.첨부파일 업로드
        //만약에 첨부파일 1개 이상이면
        if(productDto.getPimgs().size() != 0){
            productDto.getPimgs().forEach((img)->{

                FileDto fileDto =fileService.fileUpload(img);

                    ProductImgEntity productImgEntity = productimgEntityRepository.save(
                                                            ProductImgEntity.builder()
                                                                    .originalFilename(fileDto.getOriginalFilename())
                                                                    .uuidFile(fileDto.getUuidFile())
                                                                    .build()
                                                             );
                    //양방향
                productImgEntity.setProductEntity(productEntity);

                productEntity.getProductImgEntityList().add(productImgEntity);



            });

        }

        return true;
    }//end

    @Autowired
    ProductimgEntityRepository productimgEntityRepository;
    @Autowired
    FileService fileService;

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
        //삭제 할 엔티티 찾기
        Optional<ProductEntity> entityOptional =productEntityRepository.findById(id);
        //해당 엔티티가 존재하면
        entityOptional.ifPresent(o->{
            //파일도 같이 삭제
            if(o.getProductImgEntityList().size()!=0){
                o.getProductImgEntityList().forEach((img)->{
                    //1.해당 이미지 경로를 찾아서 파일 객체화
                    File file = new File(fileService.path+img.getUuidFile());
                    //2. 만약에 해당 경로의 파일이 존재하면삭제
                    if(file.exists()){
                        file.delete();

                    }
                });
            }
            //4.DB/엔티티 삭제
            productEntityRepository.delete(o);
        } );

        return false;
    }
}
