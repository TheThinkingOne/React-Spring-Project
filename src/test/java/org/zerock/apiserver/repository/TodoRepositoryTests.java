package org.zerock.apiserver.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.apiserver.domain.Todo;

import java.time.LocalDate;
import java.util.Optional;


@SpringBootTest
@Log4j2
public class TodoRepositoryTests {

    @Autowired
    private TodoRepository todoRepository; // TodoRepository wndlq

    @Test
    public void test1() {
        Assertions.assertNotNull(todoRepository);

        log.info(todoRepository.getClass().getName());
    }

    @Test
    public void testInsert() {

        for (int i = 0; i < 100; i++) {
            Todo todo = Todo.builder()
                    .title("Title.." + i)
                    .content("Content" + i)
                    .dueDate(LocalDate.of(2024,12,31))
                    .build();

            Todo result = todoRepository.save(todo); // 리턴타입 확인 잘해야함
            // 세이브 하면 리턴타입은 엔티티가 나온다

            log.info(result);
        }



    }

    @Test
    public void testRead() {

        Long tno = 1L;
        Optional<Todo> result = todoRepository.findById(tno);

        Todo todo = result.orElseThrow();

        log.info(todo);
    }

    @Test
    public void testUpdate() {

        // 일단 먼저 로딩하고 엔티티 객체를 변경 / setter
        Long tno = 1L;
        Optional<Todo> result = todoRepository.findById(tno);

        Todo todo = result.orElseThrow();

        todo.changeTitle("Update Title");
        todo.changeContent("Update Content");
        todo.changeComplete(true);

        todoRepository.save(todo);
    }

    @Test
    public void testPaging() {

        // 페이지 번호는 0부터 시작한다
        Pageable pageable =
                PageRequest.of(0, 10, Sort.by("tno").descending());

        Page<Todo> result = todoRepository.findAll(pageable);
        log.info(result.getTotalElements()); // 전체 게시글 수? 아마
        log.info(result.getContent()); // 전체 내용

        // 다음은 queryDsl 차례

    }

    // 
    /*@Test
    public void testSearch1() {
        todoRepository.search1();
    }*/



}
