package ezenweb.web.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyEntityRepository extends JpaRepository<ReplyEntity, Integer> {

        @Query(value="select * from reply where rindex=:rno", nativeQuery = true )
        List<ReplyEntity>findByrindex(int rno);


}
