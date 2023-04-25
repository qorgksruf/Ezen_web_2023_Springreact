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
                    " where if( :cno=0, cno like '%%' ,cno=:cno )"
            , nativeQuery = true)
    Page<BoardEntity> findBySearch(int cno, PageRequest pageable);

    //findBySearch

}
