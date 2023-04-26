package ezenweb.web.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardEntityRepository extends JpaRepository<BoardEntity, Integer> {


    //동일한 CNO찾기
         //select * from board where cno=?
            //ps.setInt(1,cno);
        //[JPA]select*from board where cno=:cno
            //:cno(해당 함수의 매개변수 이름)
            //cno가 0이면 조건이 없어야한다
    @Query(
            value = " select *" +
                    " from board " +
                    " where if( :cno=0, cno like '%%' ,cno=:cno ) and "+
                    " if(:key='', true , " +
                    " if(:key='btitle', btitle like %:keyword%, bcontent like %:keyword%) )"
                    //if(조건, 참,거짓if(조건,참,거짓)
            , nativeQuery = true)
    Page<BoardEntity> findBySearch(int cno,String key, String keyword, PageRequest pageable);


    //findBySearch



/*    //본인이 쓴 게시물 수정 [2023-04-26 수정하기 repository]
    @Query(
            value =" UPDATE board SET " +
                    " btitle = 'newkey' ," +
                    " bcontent='newkeyword'" +
                    " WHERE bno=4;"
            , nativeQuery = true)


    Page<BoardEntity> findBySearch(int bno,String newkey,String newkeyword);*/

}
