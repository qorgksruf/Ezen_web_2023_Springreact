package ezenweb.web.controller;

import ezenweb.web.domain.board.*;
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
/*@CrossOrigin(origins = "http://localhost:3000")*/
public class BoardController {
    //서비스 객체들
    @Autowired
    private BoardService boardService;

    //---------------view반환---------------------------------------
/*
    @GetMapping("")
    public Resource index(){
        return new ClassPathResource("templates/board/list.html");
    }*/

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
    public List<CategoryDto> categoryList(  ){  log.info("c categoryList : " );
        List<CategoryDto> result = boardService.categoryList(  );
        return result;
    }


    //2.board 게시물 쓰기 //body{"btitle":"입력한메목", "bcontent":"입력한내용","cno":선택받은 번호 }
    @PostMapping("")//요청받은 json 필드명과 dto 필드명 일치할 경우 자동 매핑
    public byte write(@RequestBody BoardDto boardDto){
        log.info("boardDto"+boardDto);
        byte result = boardService.write(boardDto);
        return result;
    }

    //카테고리별 board 게시물출력
    @GetMapping("")
    public PageDto list(PageDto pageDto ){
        //log.info("c list cno:::"+cno);
       // log.info("c list page:::"+page);
        PageDto result = boardService.list(pageDto);
        return result;
    }

    //board 개별게시물 수정 [4/26 오후1:48 백한결]
    @PutMapping("/put")
    public  boolean update(@RequestBody BoardDto boardDto){
        log.info("pageDto들어오나요?"+boardDto);

        boolean result = boardService.update(boardDto);

        return result ;
    }

    //개별출력
    @GetMapping("/getboard")
    public  BoardDto getboard(@RequestParam int bno){

        BoardDto result= boardService.print(bno);

        return result;
    }


    //3.내가 쓴 게시물 출력
    @GetMapping("/myboards")
    public List<BoardDto>myboards(){
        log.info("c myboards");
        List<BoardDto> result = boardService.myboards();
        return result;
    }


    //선택한 게시물출력
    @GetMapping("/print")
    public BoardDto print(@RequestParam int bno){
        log.info("controller에 print 들어옴? print bno:::"+bno);
        BoardDto result = boardService.print(bno);
        return result;
    }


    //3.내가 쓴 게시물 삭제
    @DeleteMapping ("")
    public boolean delete(@RequestParam int bno){
        log.info("printdelte에 bno들어옴??"+bno);

        boolean result = boardService.delete(bno);

        return result;
    }



    //-------------------쌤이랑 함께한 부분[2023-04-27 백한결]----------------------//

    //9.댓글작성
    @PostMapping("/reply")
    public boolean postReply(@RequestBody ReplyDto replyDto){

        log.info("ReplyDto"+replyDto);

       boolean result=  boardService.postReply(replyDto);
       return result;
    }

    //댓글출력
    @GetMapping("/reply")
    public boolean getReply(){
        log.info("getReply");

        return true;
    }

    //댓글수정
    @PutMapping("/reply")
    public boolean PutReply(@RequestBody ReplyDto replyDto, @RequestParam int rno){
        log.info("PutReply"+replyDto);
        log.info("PutReply"+rno);


        boolean result=  boardService.PutReply(replyDto, rno);
        log.info("reply controller 참거짓"+result);

        return result;
    }

    //댓글삭제
    @DeleteMapping("/reply")
    public boolean deleteReply(@RequestParam int rno){
        log.info("deleteReply"+rno);


        boolean result=  boardService.deleteReply(rno);
        return result;
    }



    //---------------대댓글 과제 [2023-04-27 백한결]-----------------------------------//


    //9.대댓글작성
/*    @PostMapping("/rereply")
    public boolean postRereply(@RequestBody RereplyDto rereplyDto){
        log.info("reReplyDto"+rereplyDto);

        //boolean result=  boardService.postReply(rereplyDto);
        return true;
    }
*//*
    //대댓글출력
    @GetMapping("/rereply")
    public boolean getReply(){
        log.info("getReply");

        return true;
    }*//*

    //대댓글수정
    @PutMapping("/rereply")
    public boolean PutRereply(@RequestBody RereplyDto rereplyDto){
        *//* @RequestBody RereplyDto rereplyDto, @RequestParam int rno *//*
        //log.info("PutReply"+rereplyDto);
        //log.info("PutReply"+rno);

        //boolean result=  boardService.PutReply(replyDto, rno);
        //log.info("reply controller 참거짓"+result);

        return true;
    }


    //대댓글삭제
    @DeleteMapping("/rereply")
    public boolean deleteRereply(){
        log.info("deleteRereply");
        *//*@RequestParam int rno*//*
        //boolean result=  boardService.deleteReply(rno);
        return true;
    }*/

}
