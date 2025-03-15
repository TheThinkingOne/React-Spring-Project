package org.zerock.apiserver.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CartItemListDTO {

    private Long cino; // 상품번호

    private int qty;

    private String pname;

    private int price;

    private Long pno;

    private String imageFile;

    // 생성자 추가

    public CartItemListDTO(Long cino, int qty, String pname, int price, Long pno, String imageFile) {
        this.cino = cino;
        this.qty = qty;
        this.pname = pname;
        this.price = price;
        this.pno = pno;
        this.imageFile = imageFile;
    }
    // JPA의 Projection 기능 사용해 바로 DTO 뽑기 가능

}
