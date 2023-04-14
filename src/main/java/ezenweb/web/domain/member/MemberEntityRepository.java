package ezenweb.web.domain.member;

import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberEntityRepository extends JpaRepository<MemberEntity, Integer> {
    //해당 이메일로 엔티티 찾기
        //select * from member where memail=?
    MemberEntity findByMemail(String memail);
    //해당 이메일과 비밀번호가 일치한 엔티티 반환
    //쿼리문에서 두개 and로 조건확인하는거

   Optional<MemberEntity> findByMemailAndMpassword(String memail, String mpassword);
   //[중복체크 활용]만약에 동일한 이메일이 존재하면 true 아니면 false
    boolean existsByMemail(String memail);

    // 로그인활용 만약에 동일한 이메일과 패스워드가 존재하면 true 아니면 false
   boolean existsByMemailAndMpassword(String memail, String mpassword);
   //아이디찾기[이름과 전화번호]
    Optional<MemberEntity>findByMnameAndMphone(String mname, String mphone);
   //비밀번호찾기[아이디와 전화버호]
    //boolean existsByMemailAndMphone(String memail, String mphone);

/*    //query 예시
    @Query("select * from MemberEntity m where m.memail=?1")
    MemberEntity 아이디로엔티티찾기(String memail);*/
    Optional<MemberEntity>findByMemailAndMphone(String memail, String mphone);
    //비밀번호찾기[아이디와 전화버호]
    boolean existsByMemailAndMphone(String memail, String mphone);

    //Optional<MemberEntity> findBypassword(int memail);


    //회원수정하기 과제~!!!!


}


/*
*   findById(pk값)   :해당하는 pk값이 검색후 존재하면 레코드[엔티티] 반환
*   findAll() : 모든 레코드[엔티티]반환
*   save(memberEntity) : 해당 엔티티를 DB레코드에서 innert
*   delete(memberEntity) :해당 엔티티를 DB레코드에서 delete
*   @Transactional-->setter메소드 이용: 수정
*   -----그외 추가 메소드 만들기
*   검색[레코드반환]
*       .findBy필드명(인수)
*
*          select * from member where memail=?
*
*      검색된 레코드 반환
*           Optional<MemberEntity>  :레코드/엔티티1개
*
*
* */