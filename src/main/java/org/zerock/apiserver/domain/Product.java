package org.zerock.apiserver.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity // 이걸 통해 Q 클래스가 생성됨
@Getter
@Table(name = "tbl_product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageList") // 제외시키기 이걸 안하면 테이블 조회를 2번한다?
// @ToString에서 imageList를 제외하지 않으면, toString 호출 시
// imageList를 조회하려고 **지연 로딩(Lazy Loading)**이 발생
// 이는 데이터베이스에서 추가 쿼리를 발생시킬 수 있기 때문에
// 성능 문제를 유발할 가능성이 있습니다.
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private String pname;

    private int price;

    private String pdesc;

    private boolean delFlag;

    @Builder.Default
    @ElementCollection
    private List<ProductImage> imageList = new ArrayList<>();

    // 엘리먼트 컬렉션은 주인공이 되지 않는다
    // @ElementCollection 으로 매핑된 필드는 엔티티로 취급되지 않음
    // 별도의 식별자가 없다
    // 독립적으로 존재하지 못하고 항상 소유 엔티티와 함께 저장/삭제됨
    public void changePrice(int price) {
        this.price = price;
    }

    public void changeDesc(String desc) {
        this.pdesc = desc;
    }

    public void changeName(String name) {
        this.pname = name;
    }

    public void changeDel(boolean delFlag) {
        this.delFlag = delFlag;
    }

    // 프로덕트 이미지에 관한 관리도 얘가 함
    public void addImage(ProductImage image) {

        image.setOrd(imageList.size());
        imageList.add(image);

    }

    // 이건 뭐하는 메소드인가
    public void addImageString(String fileName) {
        // 프로덕트 이미지를 만들어서 저장
        // 기존 3개의 이미지 중에서 1개만 바꿀때의 경우
        ProductImage productImage = ProductImage.builder()
                .fileName(fileName)
                .build();

        addImage(productImage);

    }

    public void clearList() {
        this.imageList.clear();
    }


}
