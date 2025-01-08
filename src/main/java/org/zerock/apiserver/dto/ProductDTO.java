package org.zerock.apiserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO { // 상품 하나에 대한 상세 정보를 표현하는 **DTO

    // DTO 를 작성할땐 어떤 용도로 사용할지 생각
    // 데이터베이스에 파일 저장하는건 권장 하진 않음

    private long pno;

    private String pname;

    private int price;

    private String pdesc;

    // 삭제는 소프트 딜리트
    private boolean delFlag;

    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>(); // 넣을때 사용

    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>(); // 조회할때 사용

}
