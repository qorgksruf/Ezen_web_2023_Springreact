package ezenweb.web.service;


import ezenweb.web.domain.board.BoardDto;
import ezenweb.web.domain.board.ReplyDto;
import ezenweb.web.domain.board.ReplyEntity;
import ezenweb.web.domain.board.ReplyEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class ReplyService {
/*
    @Autowired
    private ReplyEntityRepository replyEntityRepository;

    //댓글등록
    @Transactional
    public boolean replypost(ReplyDto replyDto){
        log.info("replyservicedp replyDto들어오는지:::"+replyDto);

        ReplyEntity replyEntity
                = replyEntityRepository.save(replyDto.toReplyEntity());

        if(replyEntity.getRno()<0){
            return false;
        }
        return true;
    }*/
/*
    //댓글삭제
    @Transactional
    private boolean delete(int mno){
        Optional<ReplyDto> entityOptional=
                ReplyEntityRepository.findById(mno);

        if(entityOptional.isPresent()){
            ReplyEntity entity = entityOptional.get();
            ReplyEntityRepository.delete(entity);
        }else{
            return false;
        }
    }*/



    //댓글출력



}
