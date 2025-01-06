package org.zerock.apiserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.apiserver.dto.ProductDTO;
import org.zerock.apiserver.util.CustomFileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 파일 업로드 관련 컨트롤러

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final CustomFileUtil fileUtil;

    @PostMapping("/")
    public Map<String, String> register(ProductDTO productDTO) {

        // 파일업로드 관련해서 꼭 JSON 으로 전송하지 않는게 RESTFUL 하지 않은건가?
        // 가이드라인일뿐 프로토콜이 있는건 아닌가?
        log.info("register: " + productDTO);

        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadedFileNames = fileUtil.saveFiles(files);

        productDTO.setUploadedFileNames(uploadedFileNames);

        log.info("uploadedFileNames: " + uploadedFileNames);

        return Map.of("RESULT", "SUCCESS");
    }


}
