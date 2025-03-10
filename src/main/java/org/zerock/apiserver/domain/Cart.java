package org.zerock.apiserver.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "owner")
@Table(
        name = "tbl_cart",
        indexes = { @Index(name="idx_cart_email", columnList = "member_owner")}
)
public class Cart {

    @Id
    @GeneratedValue
    private Long cno;

    @OneToOne // 장바구니와 사용자 매칭 (1대1)
    @JoinColumn(name = "member_owner") // Foreign Key
    private Member owner;



}
