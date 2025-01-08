package org.zerock.apiserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.apiserver.dto.PageRequestDTO;
import org.zerock.apiserver.dto.PageResponseDTO;
import org.zerock.apiserver.dto.ProductDTO;
import org.zerock.apiserver.service.ProductService;
import org.zerock.apiserver.util.CustomFileUtil;

import java.util.List;
import java.util.Map;

// 파일 업로드 관련 컨트롤러

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final CustomFileUtil fileUtil;

    private final ProductService productService;

    @PostMapping("/")
    public Map<String, String> register(ProductDTO productDTO) {

        // 파일업로드 관련해서 꼭 JSON 으로 전송하지 않는게 RESTFUL 하지 않은건가?
        // 가이드라인일뿐 프로토콜이 있는건 아닌가?
        log.info("register: " + productDTO);

        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadedFileNames = fileUtil.saveFiles(files);

        productDTO.setUploadFileNames(uploadedFileNames);

        log.info("uploadedFileNames: " + uploadedFileNames);

        return Map.of("RESULT", "SUCCESS");
    }

    // 파일(이미지) 보는 컨트롤러
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName) {

        return fileUtil.getFile(fileName);

    }

    // 검색해서 나오는 상품들 정보 가져오는 컨트롤러 메소드
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        return productService.getList(pageRequestDTO);
    }


}
