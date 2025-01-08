package org.zerock.apiserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.apiserver.domain.Product;
import org.zerock.apiserver.domain.ProductImage;
import org.zerock.apiserver.dto.PageRequestDTO;
import org.zerock.apiserver.dto.PageResponseDTO;
import org.zerock.apiserver.dto.ProductDTO;
import org.zerock.apiserver.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository; // 상품과 관련된 쿼리 클래스

    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {
        // ProductDTO :
        // PageResponseDTO : 페이지 관련 설정 DTO

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);
        // object[] => 0 product 1 productImage
        // object[] => 0 product 1 productImage
        // object[] => 0 product 1 productImage

        // a를 b로 바꾸는것이면 맵 사용가능
        List<ProductDTO> dtoList = result.get().map(arr -> { // 쿼리 결과를 ProductDTO 로 변환
            // 이 부분은 특정 상품 상세 페이지가 아니고
            // 상품 목록 페이지(예 : 농산물 카테고리)와 더 유사함
            // 쿠팡에 옥수수 쳐서 나온 페이지들을 dto 에 담아서 보내줌

            ProductDTO productDTO = null;

            Product product = (Product) arr[0]; // 상품의 데이터
            ProductImage productImage = (ProductImage) arr[1]; // 대표 이미지 데이터

            productDTO = productDTO.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();

            String imageStr = productImage.getFileName(); // 대표 이미지 파일 이름
            productDTO.setUploadFileNames(List.of(imageStr)); // 프로덕트 이미지 안에 이미지가 한개 들어가게 된다?

            return productDTO;

        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList)
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();

        //String imageStr = productRepository
    }

    // 상품 등록 메소드
    @Override
    public Long register(ProductDTO productDTO) {

        Product product = dtoToEntity(productDTO);

        log.info("------------------------");
        log.info(product);
        log.info(product.getImageList());

        Long pno = productRepository.save(product).getPno();

        return pno;
    }

    private Product dtoToEntity(ProductDTO productDTO) {

        Product product = Product.builder()
                .pno(productDTO.getPno())
                .pname(productDTO.getPname())
                .pdesc(productDTO.getPdesc())
                .price(productDTO.getPrice())
                .build();

        List<String> uploadFileNames = productDTO.getUploadFileNames();
        // 엔티티 안에 있는 컬렉션을 새로 만들면 문제가 커짐
        if(uploadFileNames == null || uploadFileNames.size() == 0) {
            return product;
        }

        uploadFileNames.forEach(fileName -> {
            product.addImageString(fileName);
        });

        return product;
    }

}
