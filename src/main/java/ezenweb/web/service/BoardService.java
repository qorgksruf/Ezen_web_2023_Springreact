package ezenweb.web.service;

import ezenweb.web.domain.board.*;
import ezenweb.web.domain.member.MemberDto;
import ezenweb.web.domain.member.MemberEntity;
import ezenweb.web.domain.member.MemberEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class BoardService {

    @Autowired
    private CategoryEntityRepository categoryEntityRepository;
    @Autowired
    private BoardEntityRepository boardEntityRepository;
    @Autowired
    private MemberEntityRepository memberEntityRepository;

    @Transactional
    public boolean categoryWrite(BoardDto boardDto) {
        log.info("service들어오는지 boardDto" + boardDto);
        //입력받은 cname을 Dto에서 카테고리 entity로 형변환해서 save
        CategoryEntity entity =
                categoryEntityRepository.save(boardDto.toCategoryEntity());
        //만약에 생성된 엔티티의 pk가 1보다 크면 save성공
        if (entity.getCno() >= 1) {
            return true;
        }
        return false;
    }

    @Transactional
    public boolean write(BoardDto boardDto) {
        log.info("service들어오는지 boardDto" + boardDto);
        //카테고리 엔티티찾기
        Optional<CategoryEntity> categoryEntityOptional = categoryEntityRepository.findById(boardDto.getCno());
        if (!categoryEntityOptional.isPresent()) {   //만약에 선택된 카테고릳가 존재하지 않으면 리턴
            return false;
        }
        CategoryEntity categoryEntity = categoryEntityOptional.get();   //카테고리 엔티티추출
        //로그인된 회원의 엔티티찾기
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 인증된 인증정보
        if (o.equals("anonymousUser")) {
            return false;
        }
        MemberDto loginDto = (MemberDto) o; //형변환
        MemberEntity memberEntity = memberEntityRepository.findByMemail(loginDto.getMemail()); //회원 엔티티찾기

        //--------------------------------------------
        //게시물 쓰기
        BoardEntity boardEntity = boardEntityRepository.save(boardDto.toBoardEntity());
        if (boardEntity.getBno() < 1) {
            return false;
        }

        categoryEntity.getBoardEntityList().add(boardEntity);
        boardEntity.setCategoryEntity(categoryEntity);

        boardEntity.setMemberEntity(memberEntity);
        memberEntity.getBoradEntityList().add(boardEntity);

        log.info(boardEntity.toString() );

        return true;
    }

    public List<BoardDto>myboards(){
        log.info("service myboards");
        return null;
    }

}