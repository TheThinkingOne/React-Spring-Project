package org.zerock.apiserver.domain;

// 엘리먼트 컬랙션으로 설계

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    private String fileName;

    private int ord; // 순번

    public void setOrd(int ord) {
        this.ord = ord;
    }



}
