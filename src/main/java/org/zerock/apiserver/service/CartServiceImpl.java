package org.zerock.apiserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.apiserver.domain.Cart;
import org.zerock.apiserver.domain.CartItem;
import org.zerock.apiserver.domain.Member;
import org.zerock.apiserver.domain.Product;
import org.zerock.apiserver.dto.CartItemDTO;
import org.zerock.apiserver.dto.CartItemListDTO;
import org.zerock.apiserver.repository.CartItemRepository;
import org.zerock.apiserver.repository.CartRepository;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO) {

        String email = cartItemDTO.getEmail();
        Long pno = cartItemDTO.getPno();
        int qty = cartItemDTO.getQty();
        Long cino = cartItemDTO.getCino();

        if (cino!=null) {
            // 장바구니 번호가 있는 경우
            Optional<CartItem> cartItemResult = cartItemRepository.findById(cino);
            CartItem cartItem = cartItemResult.orElseThrow();

            cartItem.changeQty(qty);

            cartItemRepository.save(cartItem);

            // 사용자의 모든 장바구니 아이템 반환
            return getCartItems(email); // 값이 음수가 되는 경우는?
        }

        Cart cart = getCart(email);

        // 해당 카드 안에 동일 상품이 이미 담겨있는지 점검
        CartItem cartItem = null;

        cartItem = cartItemRepository.getItemOfPno(email, pno);

        // 해당 사용자의 아이디로 해당 상품이 이미 장바구니에 담겨있는지 체크
        if (cartItem == null) { // 해당 상품이 장바구니에 없는 겨웅
            Product product = Product.builder()
                    .pno(pno)
                    .build();

            cartItem = CartItem.builder()
                    .product(product)
                    .cart(cart)
                    .qty(qty)
                    .build();
        }

        else {
            cartItem.changeQty(qty);
        }

        cartItemRepository.save(cartItem);

        return getCartItems(email);
    }

    private Cart getCart(String email) {
        // 해당 이메일의 장바구니(Cart) 가 있는지 조회 => 있으면 반환

        // 없는경우 Cart 객체 만들어서 추가(해당 이메일의 장바구니 생성)

        Cart cart = null;

        Optional<Cart> result = cartRepository.getCartOfMember(email); // 해당 이메일로 장바구니가 있느지 조회

        if(result.isEmpty()) {
            log.info("해당 회원의 장바구니가 아직 존재하지 않습니다.");

            Member member = Member.builder()
                    .email(email)
                    .build();
            Cart tempCart = Cart.builder()
                    .owner(member)
                    .build();

            cart = cartRepository.save(tempCart);

        }

        else { // 해당 회원의 장바구니가 이미 있는 경우
            cart = result.get();
        }

        return cart;
    }

    @Override
    public List<CartItemListDTO> getCartItems(String email) {
        // 사용자 장바구니에 있는 아이템 조사


        return cartItemRepository.getItemsOfCartDTOByEmail(email);
    }

    @Override
    public List<CartItemListDTO> remove(Long cino) {

        // 삭제전에 해당 장바구니 아이템이 있는 CartItem 의 상위인 Cart 의 번호 알아야 함
        Long cno = cartItemRepository.getCartFromItem(cino);

        cartItemRepository.deleteById(cino);

        return cartItemRepository.getItemsOfCartDTOByCart(cno);
    }



}
