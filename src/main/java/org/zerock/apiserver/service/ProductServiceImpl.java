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
import java.util.Optional;
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
        if(uploadFileNames == null || uploadFileNames.isEmpty()) {
            return product;
        }

        uploadFileNames.forEach(fileName -> {
            product.addImageString(fileName);
        });

        return product;
    }

    ///////////////////////////
    @Override
    public ProductDTO get(Long pno) { // 엔티티를 가져다가 DTO로 바꾸는 기능 필요

        Optional<Product> result = productRepository.findById(pno);

        Product product = result.orElseThrow();

        return entityToDto(product);
    }

    private ProductDTO entityToDto(Product product) { // 엔티티를 가져다가 DTO로 바꾸는 기능 필요

        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .delFlag(product.isDelFlag())
                .build();

        List<ProductImage> imageList = product.getImageList();

        if(imageList == null || imageList.isEmpty()) {
            return productDTO;
        }

        // 저장된 이미지의 이름 문자열 조회?
        List<String> fileNameList = imageList.stream().map(productImage ->
                productImage.getFileName()).toList();

        productDTO.setUploadFileNames(fileNameList);

        return productDTO;
        // 이미지 불러오기 어캐?
    }

    //////////////////////////////////////

    @Override // 상품 게시글 정보 변경
    public void modify(ProductDTO productDTO) {

        // 조회
        Optional<Product> result = productRepository.findById(productDTO.getPno());
        // 변경 내용 반영
        Product product = result.orElseThrow();
        // 변경 내용 저장
        product.changePrice(productDTO.getPrice());
        product.changeName(productDTO.getPname());
        product.changeDesc(productDTO.getPdesc());
        product.changeDel(productDTO.isDelFlag());

        // 이미지 처리(먼저 목록을 비워야 함)
        List<String> uploadFileNames = productDTO.getUploadFileNames();
        product.clearList();

        if(uploadFileNames != null || !uploadFileNames.isEmpty()) {

            uploadFileNames.forEach(uploadName -> {
                product.addImageString(uploadName);
            });
        }

        // 저장
        productRepository.save(product);

    }

    //////////////////////////////////////////////

    // 상품 게시글 삭제
    @Override
    public void remove(Long pno) { // 원래는 실제 삭제는 없다 원래는 진짜 삭제는 없다
        productRepository.deleteById(pno);
    }

}
