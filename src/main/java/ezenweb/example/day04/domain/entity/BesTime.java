package ezenweb.example.day04.domain.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass //상속을 주고 클래스로 이용
@EntityListeners(AuditingEntityListener.class) //해당 엔티티 실시간으로 감시한다
public class BesTime {
    @CreatedDate
    private LocalDateTime cdate; //레코드생성날짜
    @LastModifiedDate
    private LocalDateTime udate;//레코드수정날짜




}
