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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
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
    public Map<Integer,String> categoryList(){
        log.info("s categoryList");
        List <CategoryEntity> categoryEntityList = categoryEntityRepository.findAll();
       //형번환 List<엔티티>-->map
        Map<Integer,String>map=new HashMap<>();
        categoryEntityList.forEach( (e)->{
            map.put(e.getCno(),e.getCname());
        });
        return map;
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
    public List<BoardDto>list( int cno){

        List<BoardDto>list = new ArrayList<>();
        if(cno==0){//전체보기

            List<BoardEntity>boardEntityList=boardEntityRepository.findAll(); //모든 카테고리 정보 전체 출력
            boardEntityList.forEach( (e)->{
                list.add(e.toDto());
            });
            return list;

        }else{//카테고리별

            Optional<CategoryEntity>categoryEntityOptional=categoryEntityRepository.findById(cno);
            if(categoryEntityOptional.isPresent()){
                CategoryEntity categoryEntity=categoryEntityOptional.get();
                categoryEntity.getBoardEntityList().forEach( (e)->{
                   list.add(e.toDto());
                });
                return list;
            }
        }
        return null;
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
    //카테고리별 게시물출력
    @Transactional
    public BoardDto print(int bno) {
        log.info("controller에 들어옴? print bno:::" + bno);
        /*MemberDto memberDto =(MemberDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("service myboards memberDto" + memberDto);

        MemberEntity entity= memberEntityRepository.findByMemail(memberDto.getMemail())*/
        ;

        BoardEntity boardEntity = boardEntityRepository.findById(bno).get();

        return boardEntity.toDto();
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

}

