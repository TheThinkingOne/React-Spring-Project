package org.zerock.apiserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.apiserver.dto.PageRequestDTO;
import org.zerock.apiserver.dto.PageResponseDTO;
import org.zerock.apiserver.dto.ProductDTO;
import org.zerock.apiserver.service.ProductService;
import org.zerock.apiserver.util.CustomFileUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 파일 업로드 관련 컨트롤러

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final CustomFileUtil fileUtil;

    private final ProductService productService;

//    @PostMapping("/")
//    public Map<String, String> register(ProductDTO productDTO) {
//
//        // 파일업로드 관련해서 꼭 JSON 으로 전송하지 않는게 RESTFUL 하지 않은건가?
//        // 가이드라인일뿐 프로토콜이 있는건 아닌가?
//        log.info("register: " + productDTO);
//
//        List<MultipartFile> files = productDTO.getFiles();
//
//        List<String> uploadedFileNames = fileUtil.saveFiles(files);
//
//        productDTO.setUploadFileNames(uploadedFileNames);
//
//        log.info("uploadedFileNames: " + uploadedFileNames);
//
//        return Map.of("RESULT", "SUCCESS");
//    }

    // 파일(이미지) 보는 컨트롤러
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName) {

        return fileUtil.getFile(fileName);

    }

    // 검색해서 나오는 상품들 정보 가져오는 컨트롤러 메소드
    // 권한 체크해서 없으면 LIST 못들어감
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {

        log.info("list..................." + pageRequestDTO);

        return productService.getList(pageRequestDTO);

    }

    @PostMapping("/")
    // 프론트의 ProductApi.jsx 파일 참고
    public Map<String, Long> register(ProductDTO productDTO) {
        //List<MultipartFile> files = productDTO.getFiles();
        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadedFileNames = fileUtil.saveFiles(files);
        // 업로드된 파일을 리스트로 받아 처리

        productDTO.setUploadFileNames(uploadedFileNames);

        log.info("uploadedFileNames: " + uploadedFileNames);

        Long pno = productService.register(productDTO);
        // 상품을 등록하고, 상품 ID(pno)를 생성

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Map.of("RESULT", pno); // 여기서 JSON 응답 반환

    }

    @GetMapping("/{pno}") // 상품 글 불러들이는 겟매핑
    public ProductDTO read(@PathVariable("pno") Long pno) {
        return productService.get(pno);
    }

    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable Long pno, ProductDTO productDTO) {
        // 수정해서 업로드 되는 애들은 아직 저장이 안된애들임
        productDTO.setPno(pno);

        // 원래 상품(old Product) 정보 가져오기(DB에 저장되어 있는 상품에 대한 정보)
        ProductDTO oldProductDTO = productService.get(pno);

        // file 업로드
        List<MultipartFile> files = productDTO.getFiles();
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        // Keep files (이전에 있었는데 이번에 수정할 때도 계속 남아있는 파일) (String)
        // 이미지 3개중에 한개 지우면 2개는 그대로 있는것이 예시
        List<String> uploadedFileNames = productDTO.getUploadFileNames();
        if(currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileNames); // addAll 로 싹 다 넣기
        }

        productService.modify(productDTO);

        List<String> oldFileNames = oldProductDTO.getUploadFileNames();
        if(oldFileNames != null && !oldFileNames.isEmpty()) { // 있는지 없는지 먼저 찾아내기

            List<String> removeFiles = // 삭제될 애들을 리스트에 담아서
            oldFileNames.stream().filter(fileName ->
                    uploadedFileNames.indexOf(fileName) == -1).collect(Collectors.toList());
            fileUtil.deleteFiles(removeFiles); // 삭제
        } // End of if

        return Map.of("RESULT", "MODIFY SUCCESS");

    }

    @DeleteMapping("/{pno}") // 게시글 삭제 매핑
    // !! 원래는 진짜 삭제 하는건 아닌데 연습삼아 하는것임
    public Map<String, String> remove(@PathVariable Long pno) {
        List<String> oldFileNames = productService.get(pno).getUploadFileNames();

        productService.remove(pno);

        fileUtil.deleteFiles(oldFileNames);

        return Map.of("RESULT", "DELETE SUCCESS");
    }






}
