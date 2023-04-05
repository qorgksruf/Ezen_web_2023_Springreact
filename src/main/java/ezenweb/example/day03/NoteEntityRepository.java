package ezenweb.example.day03;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//엔티티 조작 인터페이스
@Repository
public interface NoteEntityRepository
            extends JpaRepository<NoteEntity, Integer> {

}
