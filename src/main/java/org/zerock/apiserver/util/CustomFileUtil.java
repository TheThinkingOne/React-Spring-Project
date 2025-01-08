package org.zerock.apiserver.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @PostConstruct // 생성자 대신 많이 씀
    public void init() {
        // 프로젝트 시작 시에 폴더 생성
        File tempFolder = new File(uploadPath);
        if(tempFolder.exists() == false) {

            tempFolder.mkdirs(); // 폴더가 없으면 생성

        }

        uploadPath = tempFolder.getAbsolutePath();
        log.info("--------------------");
        log.info("uploadPath: " + uploadPath);
    }

    // 파일 저장 관련 메소드
    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {

        if(files == null || files.size() == 0) { return List.of();} // 없으면 빈 리스트 반환

        List<String> uploadNames = new ArrayList<>(); // 저장된 파일 이름 탐색
        for(MultipartFile file : files) {
            // 파일의 이름이 같은 경우를 대비해 UUID 사용
            String savedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            Path savePath = Paths.get(uploadPath, savedName);

            // files.copy 사용하면 예외처리 필요
            try {
                Files.copy(file.getInputStream(), savePath); // 이건 원본파일 업로드 하는 부분

                String contentType = file.getContentType(); // Mime type
                // 이미지 파일이라면 섬네일로 만든다
                if (contentType != null && contentType.startsWith("image")) {
                    Path thumbNailPath = Paths.get(uploadPath, "s_" + savedName);

                    Thumbnails.of(savePath.toFile()).size(200,200).toFile(thumbNailPath.toFile());

                }


                uploadNames.add(savedName);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } // end of for

        return uploadNames;
    }

    // 이건 무슨 메소드인가?
    public ResponseEntity<Resource> getFile(String fileName) {

         Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);

         if(!resource.isReadable()) {
             resource = new FileSystemResource(uploadPath+File.separator+"default.jpeg");
         }

         HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok().headers(headers).body(resource);

    }

    // 파일 삭제 관련 메소드
    public void deleteFiles(List<String> fileNames) {
        if(fileNames == null || fileNames.isEmpty()) { return;}

        fileNames.forEach(fileName -> {
            // 썸네일 삭제
            String thumbnailFileName = "s_" + fileName;

            Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
            Path filePath = Paths.get(uploadPath, fileName);

            try {
                Files.deleteIfExists(filePath);
                Files.deleteIfExists(thumbnailPath);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }


        });
    }

}
