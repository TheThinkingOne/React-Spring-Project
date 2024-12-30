package org.zerock.apiserver.repository.search;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.apiserver.domain.QTodo;
import org.zerock.apiserver.domain.Todo;
import org.zerock.apiserver.dto.PageRequestDTO;

import java.util.List;


// 인터페이스 이름 뒤에 Impl 붙여야 한다 반드시
@Log4j2
public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch {
    public TodoSearchImpl() {
        super(Todo.class);
    }

    @Override
    public Page<Todo> search1(PageRequestDTO pageRequestDTO) {

        log.info("search1.........");

        QTodo todo = QTodo.todo;

        JPQLQuery<Todo> query = from(todo);

        //query.where(todo.title.contains("1"));

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() -1,
                pageRequestDTO.getSize(),
                Sort.by("tno").descending());
        // pageRequestDTO 는 0부터 시작
        // 스프링부트 3.0대부터 달라짐

        this.getQuerydsl().applyPagination(pageable, query);

        List<Todo> list = query.fetch(); // 목록 데이터 가져올때 사용

        long total = query.fetchCount();

        return new PageImpl<>(list, pageable, total);
    }

}
