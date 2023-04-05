package ezenweb.example.day03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//비지니스 로직 =(실질적인 업무)담당
@Service    //Mvc 서비스
@Slf4j

public class NoteService {


    @Autowired
    NoteEntityRepository noteRepository;

    public boolean write(NoteDto dto) {
        log.info("service write in"+dto);
        //Dto를 엔티티로 변환후 save
       NoteEntity entity= noteRepository.save(dto.toEntity());
       if (entity.getNno() >= 0){
           //레코드가 생성 되었으면 [등록 성공]
           return true;
       }
        return  false;
    }

    public ArrayList<NoteDto> get(){
        log.info("service get in");
        //모든 엔티티 호출
        List<NoteEntity> entityList= noteRepository.findAll(); //select 대체
        //모든 엔티티를 형변환
        ArrayList<NoteDto> list = new ArrayList<NoteDto>();

        entityList.forEach(e->{
            list.add(e.toDto() );
        });
        return  list;
    }

    public boolean delete(int nno) {
        log.info("service delete in");
        //1. 삭제할 식별번호[pk]
        Optional<NoteEntity> optionalNoteEntity = noteRepository.findById(nno); //optional 은 null 이 포함된 포장 클래스 NullPointException 검사가능
        //2.
        if (optionalNoteEntity.isPresent()) { //포장클래스내 엔티티가 들어있으면
            NoteEntity noteEntity = optionalNoteEntity.get(); //엔티티 꺼내기
            noteRepository.delete(noteEntity);
            return true;
        }

        return false;
    }


        //수정
        //@Transactional 해당 메소드내 엔티티객체 필드의 변화가 있을경우 실시간으로 commit 처리
        @Transactional //javax꺼 가져오기
        public boolean update (NoteDto dto ){
            log.info("service update in : ");
            //해당 pk 번호를 이용한 엠티티 검색
            Optional<NoteEntity>optionalNoteEntity =noteRepository.findById(dto.getNno());

            if(optionalNoteEntity.isPresent()){ //포장클래스내 엠티티가 존재하면
                NoteEntity noteEntity = optionalNoteEntity.get();   //엠티티꺼내기
               //객체내 필드변경-> 엔티티객체 필드변경->해당 레코드의 필드 값 변경
                noteEntity.setNcontents(dto.getNcontents());
                return true;
            }
            return false;
        }

    }
