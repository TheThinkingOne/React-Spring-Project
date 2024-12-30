package org.zerock.apiserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    // 슈퍼빌드 사용하는 이유
    // 상속받을때

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;
}
