package ezenweb.web.controller;

import ezenweb.web.domain.Todo.TodoDto;
import ezenweb.web.domain.Todo.TodopageDto;
import ezenweb.web.domain.member.MemberDto;
import ezenweb.web.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/todo.do")
@Slf4j
//@CrossOrigin(origins = "http://localhost:3000")//해당 컨트롤러는 http://localhost:3000 요청을 cros정책 적용
public class TodoController {
    //테스트[서비스에게 전달 받은 리스트 가정하에]

    @Autowired
    TodoService todoService;

    @GetMapping("")
    public TodopageDto get(@RequestParam int page){
        log.info("controller에 print 들어옴? TodoController page:::"+page);
        /*List<TodoDto>list = new ArrayList<>();
        list.add(new TodoDto(1,"게시물1",true));
        list.add(new TodoDto(2,"게시물2",true));
        list.add(new TodoDto(3,"게시물3",true));
*/
        //서비스 구현
        return todoService.get(page); //서비스에서 리턴 결과를 axious에게 응답
    }

    @PostMapping("")
    public boolean post(@RequestBody TodoDto todoDto){
       log.info("post todoDto:::"+todoDto);
        boolean result= todoService.post(todoDto);
        return result;
    }


    @PutMapping("")
    public boolean put(@RequestBody TodoDto todoDto){
        log.info("put todoDto :::"+todoDto);
        boolean result=todoService.put(todoDto);
        return result;
    }


    @DeleteMapping ("")
    public boolean delete(@RequestParam int id){
        log.info("delete todoDto :::"+id);
        boolean result=todoService.delete(id);
        return result;
    }

}
