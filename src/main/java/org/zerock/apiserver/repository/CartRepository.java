package org.zerock.apiserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.apiserver.domain.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // 리포지토리
    // 사용자의 장바구니에 아이템이 있는지 확인
    @Query("select cart from Cart cart where cart.owner.email = :email")
    Optional<Cart> getCartOfMember(@Param("email") String email);

}
