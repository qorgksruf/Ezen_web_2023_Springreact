package ezenweb.web.controller;

import ezenweb.web.domain.Product.ProductDto;
import ezenweb.web.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/product")
public class ProductController { /*리액트와 통신 역활[매핑]*/

    @Autowired
    public ProductService productService;

    @GetMapping("")
    public List<ProductDto> get(){
        return productService.get();
    }

    @PostMapping("")
    public boolean post(ProductDto productDto){
        return productService.post(productDto);
    }

    @PutMapping("")
    public boolean put(@RequestBody ProductDto productDto ){
        return productService.put(productDto);
    }

    @DeleteMapping("")
    public boolean delete(@RequestParam String id){
        return productService.delete(id);
    }
}
/*
    객체 전송 [post, put]
        axios.post('url',object)
            ---->@RequestBody

    폼 전송 [post 필수]
        axios.post('url',object)


    쿼리스트링 전송 [get,post,put, delete]
        axios.post('url',{params:{필드명:데이터 }})
        axios.post('url',{params:object })

    매개변수 전송[get,post,put,delete]
        axios.post('url/데이터1/데이터'

*   axios
*
* */