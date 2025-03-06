package org.zerock.apiserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.apiserver.domain.CartItem;
import org.zerock.apiserver.dto.CartItemListDTO;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 로그인한 사용자의 모든 장바구니 아이템들을 가져옴

    // 입력값 : input = email, 가져오는값 : output = CartItemListDTO
    @Query("SELECT ci, mc " +
            " FROM CartItem ci " +
            " INNER JOIN ci.cart mc ON ci.cart = mc " +
            " LEFT JOIN Product p on ci.product = p" +
            " WHERE mc.owner.email = :email")

    // ci = cart item, mc = member cart
    List<CartItemListDTO> getItemsOfCartDTOByEmail(@Param("email") String email);

    // 이메일(카카오때문에 아이디), 상품번호로 해당 상품이 장바구니 아이템으로 존재하는지 확인(중복 담기 방지용)
    // 장바구니 아이템 번호로 장바구니를 얻어오려고 하는 경우(?)

    // 장바구니 번호 모든 장바구니 아이템들 조회





}
