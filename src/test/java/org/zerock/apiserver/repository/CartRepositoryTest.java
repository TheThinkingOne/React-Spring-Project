package org.zerock.apiserver.repository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.zerock.apiserver.domain.Cart;
import org.zerock.apiserver.domain.CartItem;
import org.zerock.apiserver.domain.Member;
import org.zerock.apiserver.domain.Product;
import org.zerock.apiserver.dto.CartItemDTO;
import org.zerock.apiserver.dto.CartItemListDTO;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    public void testListOfMember() {
        String testEmail = "user1@aaa.com";

        List<CartItemListDTO> cartItemListDTOList = cartItemRepository.getItemsOfCartDTOByEmail(testEmail);

        for (CartItemListDTO cartItemListDTO : cartItemListDTOList) {
            log.info(cartItemListDTO);
        }
    }

    @Test
    @Transactional
    @Commit
    public void testInsertByProduct() {

        String email = "user1@aaa.com";
        Long pno = 25L;
        int qty = 9; // 1이었는데 늘리는 경우 테스트

        // 사용자 이메일과 상품번호로 장바구니에 아이템이 있었는지 확인
        // 없다면 추가, 있으면 수량 변경해서 추가
        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);
        if (cartItem != null) { // 이미 이 상품이 사용자의 장바구니에 담겨있음
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);

            return;
        }

        // 사용자의 장바구니에 장바구니 아이템 만들어서 저장
        // 장바구니 자체가 없을수도 있으니
        Optional<Cart> result = cartRepository.getCartOfMember(email);

        Cart cart = null;
        if (result.isEmpty()) { // 회원가입은 되어있으나 장바구니 이용해본 적 없어서 장바구니 처음으로 생성
            Member member = Member.builder()
                    .email(email)
                    .build();
            Cart tempCart = Cart.builder() // 임시 Cart 생성
                    .owner(member)
                    .build();
            cart = cartRepository.save(tempCart);
        }
        else { // 장바구니는 있는데 해당 상품의 장바구니는 없는 경우
            cart = result.get();
        }


        Product product = Product.builder()
                    .pno(pno)
                    .build();

        cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .qty(qty)
                    .build();

        cartItemRepository.save(cartItem);


    }

    @Test
    @Transactional
    @Commit
    public void testUpdateByCino() {
        Long cino = 1L;
        int qty = 4;

        Optional<CartItem> result = cartItemRepository.findById(cino);

        CartItem cartItem = result.orElseThrow();

        cartItem.changeQty(qty);

        cartItemRepository.save(cartItem);
    }

    @Test
    public void testDeleteByCino() { // cino를 기준으로 해서 해당 아이템 장바구니 삭제
        Long cino = 3L;

        Long cno = cartItemRepository.getCartFromItem(cino);

        cartItemRepository.deleteById(cno);

        // 삭제한 다음? 뭘 가져와야 하나?
        List<CartItemListDTO> cartItemList = cartItemRepository.getItemsOfCartDTOByCart(cno);

        for(CartItemListDTO cartItemListDTO : cartItemList) {
            log.info("삭제 테스트");
            log.info(cartItemListDTO);
        }

    }

}
