package ezenweb.web.service;

import ezenweb.web.domain.board.*;
import ezenweb.web.domain.member.MemberDto;
import ezenweb.web.domain.member.MemberEntity;
import ezenweb.web.domain.member.MemberEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


@Slf4j
@Service
public class BoardService {

    @Autowired
    private CategoryEntityRepository categoryEntityRepository;
    @Autowired
    private BoardEntityRepository boardEntityRepository;
    @Autowired
    private MemberEntityRepository memberEntityRepository;

    @Autowired
    private ReplyEntityRepository replyEntityRepository;


/*    @Autowired
    private RereplyEntityRepository rereplyEntityRepository;*/



    //카테고리 쓰기
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

    //모든카테고리출력
    @Transactional
    public List<CategoryDto> categoryList(  ){    log.info("s categoryList : " );
        List<CategoryEntity> categoryEntityList = categoryEntityRepository.findAll();

        List<CategoryDto> list = new ArrayList<>();
        categoryEntityList.forEach( (e)->{
            list.add( new CategoryDto( e.getCno() , e.getCname()) );
        });
        return list;
    }




    @Transactional
    public byte write(BoardDto boardDto) {
        log.info("service들어오는지 boardDto" + boardDto);
        //카테고리 엔티티찾기[ㅇ]
        Optional<CategoryEntity> categoryEntityOptional = categoryEntityRepository.findById(boardDto.getCno());
        if (!categoryEntityOptional.isPresent()) {   //만약에 선택된 카테고릳가 존재하지 않으면 리턴
            return 1;
        }
        CategoryEntity categoryEntity = categoryEntityOptional.get();   //카테고리 엔티티추출
        //로그인된 회원의 엔티티찾기
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 인증된 인증정보
        if (o.equals("anonymousUser")) {
            return 2; //즉 이건 로그인 안했다는거임
        }
        MemberDto loginDto = (MemberDto) o; //형변환
        MemberEntity memberEntity = memberEntityRepository.findByMemail(loginDto.getMemail()); //회원 엔티티찾기

        //--------------------------------------------
        //게시물 쓰기
        BoardEntity boardEntity = boardEntityRepository.save(boardDto.toBoardEntity());
        if (boardEntity.getBno() < 1) {
            return 3; //글쓰기 실패
        }
        //4.양방향관계 [카테고리안에 게시물대입, 게시물안에 카테고리 힙[주소] 대입]
        categoryEntity.getBoardEntityList().add(boardEntity);
        boardEntity.setCategoryEntity(categoryEntity);
        //5.양방향 관계
        boardEntity.setMemberEntity(memberEntity);
        memberEntity.getBoradEntityList().add(boardEntity);


        //공지사항 게시물 정보 확인


        Optional<CategoryEntity>optionalCategoryEntity = categoryEntityRepository.findById(4);
        log.info("공지사항 카테고리 엔티티확인:::"+optionalCategoryEntity.get());



        return 4;
    }

    //카테고리별 게시물 출력
    @Transactional
    public PageDto list( PageDto pageDto ){

        log.info("pageDto boardService 검색눌렀을때:::"+pageDto);
        //1.pageable 인터페이스 [페이징처리 관련 api]
            // domain 사용
        PageRequest pageable = PageRequest.of(pageDto.getPage()-1,3, Sort.by(Sort.Direction.DESC,"bno") ); //즉 페이지당 10개씩 출력을 의미함
                //PageRequest.of(페이지번호[0시작],페이지당표시개수)   //Sort.by: 정렬기준필드명 bno기준으로 정렬하겟다는거임
        Page<BoardEntity> entityPage= boardEntityRepository.findBySearch(pageDto.getCno(), pageDto.getKey(), pageDto.getKeyword(),pageable);
        //

        List<BoardDto>boardDtoList= new ArrayList<>();
        entityPage.forEach( (b) ->{
            boardDtoList.add(b.toDto());
        });

        log.info("총게시물수:"+entityPage.getTotalElements());
        log.info("총페이지수:"+entityPage.getTotalPages());

        pageDto.setBoardDtoList( boardDtoList );
        pageDto.setTotalCount( entityPage.getTotalElements() );
        pageDto.setTotalPage( entityPage.getTotalPages() );


        return pageDto;
    }


    //내가 슨 게시물 출략
    @Transactional
    public List<BoardDto>myboards(){
        log.info("service myboards");
        //1.로그인 인증 세션 호출-->OBJECT니까 DTO로 강제형변환
        MemberDto memberDto =(MemberDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       //일반회원dto: 모든정보,oauth2dto:memail,mname
        log.info("service myboards memberDto" + memberDto);
        //Object o =SecurityContextHolder.getContext().getAuthentication().getPrincipal();

       //2.회원 엔티티 찾기
        MemberEntity entity= memberEntityRepository.findByMemail(memberDto.getMemail());
        //3.회원 엔티티 내 게시물리스트를 바복문 돌려서 dto 리스트로 변환
        List<BoardDto>list=new ArrayList<>();
        entity.getBoradEntityList().forEach( (e)->{
            list.add(e.toDto());
        });
        return list;
    }
    //과제
    //카테고리별 게시물출력 + 댓글출력
    @Transactional
    public BoardDto print(int bno) {
        log.info("controller에 들어옴? print bno:::" + bno);

        Optional<BoardEntity>optionalBoardEntity = boardEntityRepository.findById(bno);

        if(optionalBoardEntity.isPresent()){

            BoardEntity boardentity = optionalBoardEntity.get();

            List<ReplyDto>list = new ArrayList<>();

            boardentity.getReplyEntityList().forEach((r)->{
                list.add(r.toDto());
            });
           BoardDto boardDto  = boardentity.toDto();
           boardDto.setReplyDtoList(list);
           return boardDto;
        }

        return null;
/*        BoardEntity boardEntity = boardEntityRepository.findById(bno).get();

        return boardEntity.toDto();*/
    }

    @Transactional
    public boolean delete(int bno) {
        log.info("service delte에 bno들어옴??"+bno);
        MemberDto memberDto=(MemberDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            log.info("memberDto의 세션 아이디확인"+memberDto.getMemail());

        BoardEntity entity=boardEntityRepository.findById(bno).get();
            log.info("entity확인"+entity);

        if(memberDto.getMemail().equals(entity.getMemberEntity().getMemail())){

            boardEntityRepository.delete(entity);
            return true;
        }

            return false;
    }

    @Transactional
    //board 개별게시물 수정 [4/26 오후1:48 백한결]
    public  boolean update(BoardDto boardDto){
        log.info("boardDto들어오나요? 여긴 service입닌다"+boardDto);

        Optional<BoardEntity> entityOptional
                = boardEntityRepository.findById( boardDto.getBno());

       if(entityOptional.isPresent()){
           BoardEntity boardEntity = entityOptional.get();
           boardEntity.setBtitle(boardDto.getBtitle());
           boardEntity.setBcontent(boardDto.getBcontent());
           return true;
       }
        return false ;
    }








    //board 댓글 만들기 [2023-04-27 수업 쌤과]



    //9.댓글작성

    @Transactional
    public boolean postReply( ReplyDto replyDto){
        log.info("ReplyDto"+replyDto);

        //로그인했는지
        Object o= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(o.equals("anonymousUser")){
            return false;
        }
        MemberDto memberDto =(MemberDto)o;
        MemberEntity memberEntity=memberEntityRepository.findById(memberDto.getMno() ).get();


        //댓글 작섷할 게시물 호출
        Optional <BoardEntity> optionalBoardEntity= boardEntityRepository.findById( replyDto.getBno() );
        if(!optionalBoardEntity.isPresent()) {
            return false;
        }
        BoardEntity boardEntity=optionalBoardEntity.get();


        //댓글 작성한다
        ReplyEntity replyEntity = replyEntityRepository.save(replyDto.toReplyEntity());

        if(replyEntity.getRno()<1){
            return false;
        }

        //댓글과 회원의 양방향관계
        replyEntity.setMemberEntity(memberEntity);
        memberEntity.getReplyEntityList().add(replyEntity);

        replyEntity.setBoardEntity(boardEntity);
        boardEntity.getReplyEntityList().add(replyEntity);

        return true;
    }

    //댓글출력
    @Transactional
    public boolean getReply(){ //게시물 출력시로  대체함
        return true;
    }

    //댓글수정
    @Transactional
    public boolean PutReply( ReplyDto replyDto , int rno){ //{rno: 수정할번호 rcontent:수정내용}
        log.info("service rno"+rno);
        log.info("PutReply"+replyDto);


        Optional<ReplyEntity>optionalReplyEntity =
                    replyEntityRepository.findById(rno);
        if(optionalReplyEntity.isPresent()){
            optionalReplyEntity.get().setRcontent(replyDto.getRcontent());
            return true;
        }
        return false;
    }


    //댓글삭제
    @Transactional
    public boolean deleteReply(int rno){

        Optional< ReplyEntity >optionalReplyEntity =
                    replyEntityRepository.findById(rno);
               // log.info("service의 rindex"+rindex);


        if(optionalReplyEntity.isPresent()){

                replyEntityRepository.delete(optionalReplyEntity.get());

                List<ReplyEntity>replyEntities = replyEntityRepository.findByrindex(rno);


                    replyEntities.forEach((r)->{
                        replyEntityRepository.delete(r);
                    });

            return true;
        }

        return false;
    }
/*

    //----------------------------대댓글과제 [2023-04-27]-----------------------------//


    //9.대댓글작성
    @Transactional
    public boolean postRereply(RereplyDto rereplyDto){
        log.info("reReplyDto"+rereplyDto);
        log.info("ReplyDto"+rereplyDto);

        //로그인했는지
        Object o= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(o.equals("anonymousUser")){
            return false;
        }

        //형변환
        MemberDto memberDto =(MemberDto)o;
        MemberEntity memberEntity=memberEntityRepository.findById(memberDto.getMno() ).get();


        //댓글 작섷할 게시판 호출
        Optional <BoardEntity> optionalBoardEntity= boardEntityRepository.findById( rereplyDto.getBno() );
        if(!optionalBoardEntity.isPresent()) {
            return false;
        }
        BoardEntity boardEntity=optionalBoardEntity.get();


        //댓글 작섷할 게시물 호출
        Optional <ReplyEntity> optionalReplyEntity = replyEntityRepository.findById( rereplyDto.getRno() );

        if(!optionalReplyEntity.isPresent()) {
            return false;
        }

        ReplyEntity replyEntity = optionalReplyEntity.get();




        //대댓글 작성한다
        RereplyEntity rereplyEntity = rereplyEntityRepository.save(rereplyDto.toRereplyEntity());

        if(rereplyEntity.getReno()<1){
            return false;
        }

        //댓글과 회원의 양방향관계
        replyEntity.setMemberEntity(memberEntity);
        memberEntity.getReplyEntityList().add(replyEntity);

        replyEntity.setBoardEntity(boardEntity);
        boardEntity.getReplyEntityList().add(replyEntity);


        rereplyEntity.setReplyEntity(replyEntity);
        replyEntity.getRereplyEntityList().add(rereplyEntity);



        return true;


    }

*/
/*
    //대댓글출력
    @GetMapping("/rereply")
    public boolean getReply(){
        log.info("getReply");

        return true;
    }*//*


    //대댓글수정
    @Transactional
    public boolean PutRereply( RereplyDto rereplyDto){
        */
/* @RequestBody RereplyDto rereplyDto, @RequestParam int rno *//*

        //log.info("PutReply"+rereplyDto);
        //log.info("PutReply"+rno);

        //boolean result=  boardService.PutReply(replyDto, rno);
        //log.info("reply controller 참거짓"+result);

        return true;
    }


    //대댓글삭제
    @Transactional
    public boolean deleteRereply(){
        log.info("deleteReply");
        */
/*@RequestParam int rno*//*

        //boolean result=  boardService.deleteReply(rno);
        return true;
    }


*/

}


