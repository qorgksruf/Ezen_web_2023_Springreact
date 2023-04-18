package ezenweb.web.controller;

import ezenweb.web.domain.board.BoardDto;
import ezenweb.web.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/board")
public class BoardController {
    //서비스 객체들
    @Autowired
    private BoardService boardService;

    //---------------view반환---------------------------------------

    @GetMapping("")
    public Resource index(){
        return new ClassPathResource("templates/board/list.html");
    }

    //--------------modal 반횐--------------------------------------------//
    //1.카테고리 등록
    @PostMapping("/category/write")
    public boolean categoryWrite(@RequestBody BoardDto boardDto){
        log.info("c boardDto"+boardDto);
        boolean result = boardService.categoryWrite(boardDto);
        return result;
    }

    //카테고리출력[반환타입: {1: 공지사항 2.자유게시판 }]
            //List:{값,값,값,값}
            //Map:{키:값, 키:값, 키:값}
    @GetMapping("/category/list")
    public Map<Integer,String> categoryList(){
        log.info("c categoryList");
        Map<Integer,String>result = boardService.categoryList();
        return result;
    }


    //2.게시물 쓰기 //body{"btitle":"입력한메목", "bcontent":"입력한내용","cno":선택받은 번호 }
    @PostMapping("/write")//요청받은 json 필드명과 dto 필드명 일치할 경우 자동 매핑
    public byte write(@RequestBody BoardDto boardDto){
        log.info("boardDto"+boardDto);
        byte result = boardService.write(boardDto);
        return result;
    }
    //카테고리별 게시물출력
    @GetMapping("/list")
    public List<BoardDto>list(@RequestParam int cno){
        log.info("c list cno:::"+cno);
        List<BoardDto> result = boardService.list(cno);
        return result;
    }

    //3.내가 쓴 게시물 출력
    @GetMapping("/myboards")
    public List<BoardDto>myboards(){
        log.info("c myboards");
        List<BoardDto> result = boardService.myboards();
        return result;
    }


    //카테고리별 게시물출력
    @GetMapping("/print")
    public BoardDto print(@RequestParam int bno){
        log.info("controller에 print 들어옴? print bno:::"+bno);
        BoardDto result = boardService.print(bno);
        return result;
    }


    //3.내가 쓴 게시물 삭제
    @DeleteMapping ("/delete")
    public boolean delete(@RequestParam int bno){
        log.info("printdelte에 bno들어옴??"+bno);

        boolean result = boardService.delete(bno);

        return result;
    }

}