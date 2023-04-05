package ezenweb.example.day03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.*;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;

@RestController//MVC 컨트롤러
@Slf4j//로그
@RequestMapping("/note")//공통 url

public class NoteController {
    @Autowired  //생성자 자동 주입[단 스프링 컨테이너 등록된 애만 가능]
    NoteService noteService;    //서비스 객체 생성

    @PostMapping("/write")
    public boolean write(@RequestBody NoteDto noteDto) {
        log.info("write in::" + noteDto);
        //@Autowired
        boolean result = noteService.write(noteDto);
        //*response.getwrite.print()
        return result;
    }
    //------------html 반환
    @GetMapping("")
    public Resource index() {
        return new ClassPathResource("templates/note.html");
    }


    @GetMapping("/get")
    public ArrayList<NoteDto> get() {
        ArrayList<NoteDto>result = noteService.get();
        log.info("get in");
        return result;
    }

    // 3. 삭제
    @DeleteMapping("/delete")
    public boolean delete(@RequestParam int nno) {
        log.info( "delete in : " + nno );
        boolean result = noteService.delete(nno);
        return result;
    }

    // 4. 수정
    @PutMapping("/update")
    public boolean update(@RequestBody NoteDto dto) {
        log.info( "update in : " + dto );
        boolean result = noteService.update(dto);
        return result;
    }
}


