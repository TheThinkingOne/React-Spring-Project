package org.zerock.apiserver.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"cart"}) // ToString 할때 연관관계를 빼라는건 대체 무슨 뜻일까?
@Table(
        name = "tbl_cart_item", indexes = {
                @Index(columnList = "cart_cno", name = "idx_cartitem_cart"),
        @Index(columnList = "product_pno, cart_cno", name = "idx_cartitem_pno_cart")
})
// 카트번호를 통해 장바구니 안의 상품을 조회할 것 => 카트번호 많이 사용
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cino; // 장바구니 안 상품의 번호

    // CartItem 엔티티에서 Product 엔티티와 다대일(@ManyToOne) 관계를 설정
    // 외래키(FK)로 product_pno 컬럼 사용 선언
    @ManyToOne
    @JoinColumn(name = "product_pno")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_cno")
    private Cart cart;

    private int qty; // 수량

    private void changeQty(int qty) {
        this.qty = qty;
    }


}
