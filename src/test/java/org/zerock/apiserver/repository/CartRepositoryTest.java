package org.zerock.apiserver.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.apiserver.dto.CartItemDTO;

@SpringBootTest
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    public void testListOfMember() {
        String testEmail = "user1@aaa.com";

        cartItemRepository.getItemsOfCartDTOByEmail(testEmail);
    }

}
