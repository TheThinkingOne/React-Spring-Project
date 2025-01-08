package org.zerock.apiserver.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.apiserver.dto.PageRequestDTO;
import org.zerock.apiserver.dto.PageResponseDTO;
import org.zerock.apiserver.dto.ProductDTO;

import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        PageResponseDTO<ProductDTO> responseDTO = productService.getList(pageRequestDTO);

        log.info(responseDTO.getDtoList());
    }

    @Test
    public void testRegister() { // tbl_product 테이블에 신규 상품 삽입
        ProductDTO productDTO = ProductDTO.builder()
                .pname("새로운 상품 예시")
                .pdesc("신규 추가 상품입니다")
                .price(10000)
                .build();

        // UUID 가 있어야 한다
        productDTO.setUploadFileNames(
                java.util.List.of(
                        UUID.randomUUID()+"_" + "Test1.jpg",
                        UUID.randomUUID()+"_" + "Test2.jpg"));

        productService.register(productDTO);

    }

}