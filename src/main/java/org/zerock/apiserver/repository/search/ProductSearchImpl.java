package org.zerock.apiserver.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.apiserver.domain.Product;
import org.zerock.apiserver.domain.QProduct;
import org.zerock.apiserver.domain.QProductImage;
import org.zerock.apiserver.dto.PageRequestDTO;
import org.zerock.apiserver.dto.PageResponseDTO;
import org.zerock.apiserver.dto.ProductDTO;

import java.util.List;
import java.util.Objects;

@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl()  {
        super(Product.class);
    }

    @Override
    public PageResponseDTO<ProductDTO> searchList(PageRequestDTO pageRequestDTO) {

        log.info("--------------------------- searchList acting???");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() -1, // 페이지 번호
                pageRequestDTO.getSize(), // 페이지 request dto 의 크기
                Sort.by("pno").descending()
        );

        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;

        // JPQL 쿼리 생성(목록 처리 관련)
        JPQLQuery<Product> query = from(product);

        query.leftJoin(product.imageList, productImage); // product.imageList 를 productImage 로 간주할것?

        query.where(productImage.ord.eq(0)); // 0번째 이미지(대표이미지 가져오기)

        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);

        List<Tuple> productList = query.select(product, productImage).fetch();

        //getQuerydsl().applyPagination(pageable, query);

        //List<Product> productList = query.fetch();

        long count = query.fetchCount();

        log.info("=====================================");
        log.info(productList);

        return null;
    }
}
