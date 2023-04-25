package ezenweb.web.service;



import ezenweb.web.domain.Todo.TodoDto;
import ezenweb.web.domain.Todo.TodoEntity;
import ezenweb.web.domain.Todo.TodoentityRepository;
import ezenweb.web.domain.Todo.TodopageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TodoService {

    @Autowired
    private TodoentityRepository todoEntityRepository;

    @Transactional
    public TodopageDto get( int page){

        PageRequest pageable = PageRequest.of(page-1,3);

        Page<TodoEntity> entityPage =todoEntityRepository.findAll(pageable);

        List<TodoDto>list = new ArrayList<>();

        entityPage.forEach( (e) -> {
            list.add (e.todoDto());
        });
        //서비스 구현
        return TodopageDto.builder()
                .totalCount(entityPage.getTotalElements())
                .totalPage(entityPage.getTotalPages())
                .TodoDtoList(list)
                .page(page)
                .build();
    };



    @Transactional
    public boolean post(TodoDto todoDto){
        log.info("post todoDto:::"+todoDto);

        TodoEntity entity
                = todoEntityRepository.save(todoDto.todoEntity());

        if(entity.getId() < 0){
            return false;
        }
        return true;
    }



    @Transactional
    public boolean put( TodoDto todoDto){
        log.info("put todoDto :::"+todoDto);
        Optional<TodoEntity>todoEntityOptional =todoEntityRepository.findById(todoDto.getId());
        if (todoEntityOptional.isPresent()) {   //만약에 선택된 카테고릳가 존재하지 않으면 리턴
            TodoEntity todoEntity = todoEntityOptional.get();

            todoEntity.setTitle(todoDto.getTitle());

            return true;
        }

        return false;
    }


    @Transactional
    public boolean delete( int id){

        Optional<TodoEntity>todoEntityOptional =todoEntityRepository.findById(id);
        if (todoEntityOptional.isPresent()) {   //만약에 선택된 카테고릳가 존재하지 않으면 리턴
            TodoEntity todoEntity = todoEntityOptional.get();
            todoEntityRepository.delete(todoEntity);
            return true;
        }

        return false;
    }

}
