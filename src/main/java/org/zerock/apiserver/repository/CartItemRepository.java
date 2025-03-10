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
    @Query("SELECT" +
            " new org.zerock.apiserver.dto.CartItemListDTO(ci.cino, ci.qty, p.pname, p.price, pi.fileName) " +
            " FROM CartItem ci " +
            " INNER JOIN Cart mc ON ci.cart = mc " +
            " LEFT JOIN Product p on ci.product = p" +
            " LEFT JOIN p.imageList pi " +
            " WHERE " +
            " pi.ord = 0 " +
            " AND mc.owner.email = :email " +
            " ORDER BY ci.cino DESC")
    // Long cino, int id, String pname, int price, String imageFile
    // 와 저게 된다고? 이게 DTO Projection 인가
    // ci = cart item, mc = member cart
    List<CartItemListDTO> getItemsOfCartDTOByEmail(@Param("email") String email);

    // 이메일(카카오때문에 아이디), 상품번호로 해당 상품이 장바구니 아이템으로 존재하는지 확인(중복 담기 방지용)

    @Query("SELECT ci FROM CartItem ci LEFT JOIN Cart c ON ci.cart = c" +
            " WHERE c.owner.email = :email AND ci.product.pno = :pno")
    CartItem getItemOfPno(@Param("email") String email, @Param("pno") Long pno);

    // 장바구니 아이템 번호로 장바구니 번호를 얻어오려고 하는 경우
    @Query("SELECT c.cno FROM Cart c LEFT JOIN CartItem ci on ci.cart = c" +
            " WHERE ci.cino = :cino")
    Long getCartFromItem(@Param("cino") Long cino);


    // 장바구니 번호로 모든 장바구니 아이템들 조회
    @Query("SELECT " +
            "new org.zerock.apiserver.dto.CartItemListDTO(ci.cino, ci.qty, p.pname, p.price, pi.fileName ) " +
            "FROM CartItem ci" +
    " INNER JOIN Cart mc on ci.cart = mc" +
    " LEFT JOIN Product p on ci.product = p" +
    " LEFT JOIN p.imageList pi" +
    " WHERE " +
    " pi.ord = 0 AND mc.cno = :cno "+
    " ORDER BY ci.cino DESC")
    List<CartItemListDTO> getItemsOfCartDTOByCart(@Param("cno") Long cno);




}
