package org.zerock.apiserver.repository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.apiserver.domain.Product;
import org.zerock.apiserver.dto.PageRequestDTO;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testInsert() { // 자료 삽입

        for (int i=0; i<10; i++) {
            Product product = Product.builder()
                    .pname("Test ")
                    .pdesc("Test description")
                    .price(9999)
                    .build();

            product.addImageString(UUID.randomUUID()+"_"+"IMAGE1.jpg");
            product.addImageString(UUID.randomUUID()+"_"+"IMAGE2.jpg");
            productRepository.save(product);
        }

    }

    @Test
    @Transactional // 이거 안쓰면 노 세션 에러가 발생
    // imageList 는 @ElementCollection 이므로 lazy loading 설정이 기본값
    // 따라서 트랜잭션 범위 밖에서 imageList 에 접근하면 세션이 닫혀 오류 발생
    // @Transactional을 사용하면, 트랜잭션 범위 내에서 엔티티를 사용할 수 있으므로
    // 문제를 방지할 수 있습
    public void testRead() {

        Long pno = 1l;
        Optional<Product> result = productRepository.findById(pno);

        Product product = result.orElseThrow();

        log.info(product);

        log.info(product.getImageList());

        // 조인을 자기가 알아서 하게끔 만드는?

    }

    @Test
    public void testRead2() { // 여긴 트랜젝션 어노테이션 안씀
        // @EntityGraph를 사용한 selectOne 메서드는 즉시 로딩을 수행하므로
        // @Transactional 없이도 정상적으로 동작
        // 이 테스트 실행하면 위의 testRead 테스트와 다르게 쿼리가 한번만 실행됨
        // 자동으로 지가 조인해서 보여줌


        Long pno = 1l;
        Optional<Product> result = productRepository.selectOne(pno);

        Product product = result.orElseThrow();

        log.info(product);

        log.info(product.getImageList());

        // 조인을 자기가 알아서 하게끔 만드는?

    }

    @Commit
    @Transactional // 삭제
    @Test
    public void testDelete() { // 삭제(soft delete) 테스트
        Long pno = 2L;
        productRepository.updateToDelete(2L, true);
    }

    @Test
    public void testUpdate() { // 엘리먼트 컬렉션
        Product product = productRepository.selectOne(1L).get();

        product.changePrice(15000);

        product.clearList();
        // 근데 이렇게 안하고 클리어리스트를 안하고 그냥 다른 ArrayList 로 바꿔치면 안되는것?
        // 바로 이겻이 맹점
        // 얘가 물고 잇는 컬렉션을 계속 쓰셔야 합니다!!
        product.addImageString(UUID.randomUUID()+"_"+"IMAGE1.jpg");
        product.addImageString(UUID.randomUUID()+"_"+"IMAGE2.jpg");
        product.addImageString(UUID.randomUUID()+"_"+"IMAGE3.jpg");
        productRepository.save(product);

        // 라이프사이클이 엔티티에 맞춰진다
        // select update delete insert 순으로 되는걸 확인할수있음
    }

    @Test
    public void testList() { // 삽입되어 있는 물품 리스트 뽑아오는 테스트
        Pageable pageable = PageRequest.of(0,10, Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);

        result.getContent().forEach(arr -> log.info(Arrays.toString(arr)));
        // 실행하면 조인 처리 되서 결과가 나온다

    }

    @Test
    public void testSearch() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        productRepository.searchList(pageRequestDTO);
    }

}
