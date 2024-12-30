package org.zerock.apiserver.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Length;

import java.time.LocalDate;

@Entity
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "tbl_todo") // 테이블 이름 지정
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 키 생성 전략
    private Long tno;

    @Column(length = 500, nullable = false)
    private String title;

    private String content;

    private boolean complete;

    private LocalDate dueDate;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeComplete(boolean complete) {
        this.complete = complete;
    }

    public void changeDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
