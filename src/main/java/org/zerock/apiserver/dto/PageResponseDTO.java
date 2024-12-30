package org.zerock.apiserver.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<E> {

    private List<E> dtoList; // 현재 페이지에 표시될 데이터 목록

    private List<Integer> pageNumList; // 현재 페이지 주변에 표시될 페이지 번호 목록
    // ex [1,2,3,4,5,6,7,...]

    // 검색조건 현제페이지 이런건 어디있나
    private PageRequestDTO pageRequestDTO; // 요청받은 페이지와 관련된 정보(PageRequestDTO 객체)가 저장
    // ex : 현재 페이지 번호, 페이지 크기 등

    private boolean prev, next; // 이 다음 페이지의 이전, 다음이 있는지 체크하는 불린

    private int totalCount, prevPage, nextPage, totalPage, current;
    // totalCount : 전체 개시글 수, totalPage : 전체 페이지의 수
    // ex 전체 개시글이 132개면 totalCount = 132, totalPage = 14

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long total) {
        // 페이지에 관환 정보를 담는 DTO 생성자

        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.totalCount = (int) total; // 다운캐스팅

        // 페이지네이션 계산

        // 끝 페이지 번호 계산
        int end = (int) (Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10;

        // 시작 페이지 번호 계산
        int start = end - 9;

        // 맨 마지막 페이지
        int last = (int) (Math.ceil(totalCount / (double) pageRequestDTO.getSize()));

        // 끝 번호(end)가 실제 마지막 페이지 번호(last)보다 크다면, 마지막 페이지로 조정
        end = end > last ? last : end;

        // 이전 페이지 존재 여부
        this.prev = start > 1;

        // 다음 페이지 존재 여부
        this.next = totalCount > end * pageRequestDTO.getSize();
        // 전체 데이터 개수가 현재 그룹 끝 페이지를 기준으로
        // 보여질 수 있는 데이터 개수보다 많으면 다음 페이지가 존재

        // 박싱 사용
        // 페이지 번호 리스트 생성
        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
        // start부터 end까지의 숫자를 리스트로 생성하여 페이지 번호 리스트 생성

        // 이전 페이지 번호 설정, 없으면 0으로 설정
        this.prevPage = start > 1 ? start -1 : 0;

        // next 가 true 이다 => 다음 페이지가 있다
        // 다음 페이지 번호 설정, 없으면 0으로 설정
        this.nextPage = next ? end + 1 : 0; //

    }

}
