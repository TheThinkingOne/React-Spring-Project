package org.zerock.apiserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.apiserver.domain.Product;
import org.zerock.apiserver.repository.search.ProductSearch;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {

    @EntityGraph(attributePaths = "imageList") // 이거 쓰면 같이 로징한다
    // SELECT 하고나서 그걸로 UPDATE 하는 두번 일 대신 한번에 조인해서 한 쿼리로 됨
    @Query("select p from Product p where p.pno = :pno")
    Optional<Product> selectOne(@Param("pno") Long pno);

    // Modifying Annotation 이건뭐징
    @Modifying
    // @Modifying은 JPQL 또는 네이티브 쿼리에서
    // UPDATE나 DELETE 작업을 수행할 때 사용
    // 단순 작업이 아닌 데이터베이스를 변경하는 작업임을 JPA 에 알려주는 역할 함
    @Query("update Product p set p.delFlag = :delFlag where p.pno = :pno")
    void updateToDelete(@Param("pno") Long pno, @Param("delFlag") boolean flag);

    // 엘리먼트 컬렉션도 조인 가능
    @Query("select p, pi from Product p left join p.imageList pi where pi.ord = 0 and p.delFlag = false")
    Page<Object[]> selectList(Pageable pageable); // 여기서 메소드 이름이 쿼리문 이름



}
