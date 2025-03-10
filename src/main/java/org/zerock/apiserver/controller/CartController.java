package org.zerock.apiserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zerock.apiserver.dto.CartItemDTO;
import org.zerock.apiserver.dto.CartItemListDTO;
import org.zerock.apiserver.service.CartService;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Log4j2
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PreAuthorize("#itemDTO.email == authentication.name")
    // 위처럼 #을 해서 직접 DTO에 있는 값과 인증에서 넘어온 값이 같은지 비교할 수 있다 우와!
    @PostMapping("/change")
    public List<CartItemListDTO> changeCart(@RequestBody CartItemDTO itemDTO) {
        log.info("현재 itemDTO : " + itemDTO);

        if (itemDTO.getQty() <= 0) {
            // 사용자가 수량 변경을 눌러서 0 이하로 만든다면
            return cartService.remove(itemDTO.getCino());
        }

        return cartService.addOrModify(itemDTO);
    }
    // return 타입이 ListDTO 인 이유 => 변경된 후 현재 장바구니에 있는 모든 장바구니의
    // 목록 아이템들은 전부 다 보여줄 것이니까

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/items")
    public List<CartItemListDTO> getCartItems(Principal principal) {

        String email = principal.getName();
        log.info("Security principal 에서 불러온 email : " + email);
        //return cartService.getCartItems(principal.getName());
        return cartService.getCartItems(email);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{cino}")
    public List<CartItemListDTO> removeFromCart(@PathVariable("cino") Long cino) {
        log.info("cart item 번호 : " + cino);
        return cartService.remove(cino);
    }

}
