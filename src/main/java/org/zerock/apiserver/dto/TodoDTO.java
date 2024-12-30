package org.zerock.apiserver.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// DTO 는 업무와 관련이 있다
// !! DTO를 앤터티로 바꾸거나 앤터티를 DTO로 바꾸는건 서비스 계층에서 수행
public class TodoDTO {
    // Data Transfer Objective
    private Long tno;

    // @Column(length = 500, nullable = false)
    private String title;

    private String content;

    private boolean complete;

    private LocalDate dueDate;

}
