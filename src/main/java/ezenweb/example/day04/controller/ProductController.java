package ezenweb.example.day04.controller;

import ezenweb.example.day03.NoteDto;
import ezenweb.example.day04.domain.dto.ProductDto;
import ezenweb.example.day04.domain.entity.product.ProductEntity;
import ezenweb.example.day04.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController //컨트롤러
@RequestMapping("/item")
public class ProductController {

    //컨테이너에 등록된 빈 자동주입
    @Autowired
    private ProductService service;

    // 1.[react.js 사용하기전 HTML] 반환
    @GetMapping("")
    public Resource index(){
        return new ClassPathResource("templates/item.html");

    }

    @GetMapping("/get")
    public ArrayList<ProductDto> get() {
        ArrayList<ProductDto>result = service.get();
        return result;
    }

    //2. Restful api 기능처리 요청응답
        //저장
    @PostMapping("/write")
    public boolean write(@RequestBody ProductDto dto){
        return service.write(dto);
    }
    //수정
    @PutMapping("/update")
    public boolean update(@RequestBody ProductDto dto){
        return service.update(dto);
    }

    //삭제
    @DeleteMapping("/delete")
    public boolean itemdelete(@RequestParam int pno){
        return service.itemdelete(pno);
    }



    //3.유효성검사   실질적 기능을 서비스에서 하지만 그 전의 겸문으로 controller



}
