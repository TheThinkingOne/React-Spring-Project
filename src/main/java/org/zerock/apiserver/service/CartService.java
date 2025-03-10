package org.zerock.apiserver.service;

import org.springframework.transaction.annotation.Transactional;
import org.zerock.apiserver.dto.CartItemDTO;
import org.zerock.apiserver.dto.CartItemListDTO;

import java.util.List;

@Transactional
public interface CartService {

    List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO); // 사실 변경이 아니고 삭제?

    // 누군가 로그인
    List<CartItemListDTO> getCartItems(String email);

    List<CartItemListDTO> remove(Long cino);
}
