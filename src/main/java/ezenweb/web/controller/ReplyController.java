package ezenweb.web.controller;


import ezenweb.web.domain.board.ReplyDto;
import ezenweb.web.domain.board.ReplyEntity;
import ezenweb.web.service.BoardService;
import ezenweb.web.service.ReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@Slf4j
@RequestMapping("/reply")
public class ReplyController {

    @Autowired
    private ReplyService replyService;


    @PostMapping("")
    public boolean replypost(@RequestBody ReplyDto replyDto){
        log.info("replyController replyDto들어오는지:::"+replyDto);

        boolean result
                  = replyService.replypost(replyDto);

        return result;
    }

    //삭제
    @DeleteMapping("")
    public boolean delete(@RequestParam int mno){
        log.info("replyController delete에 들어오는지"+mno);

        return false;


    }

}
