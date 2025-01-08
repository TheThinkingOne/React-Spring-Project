package org.zerock.apiserver.repository.search;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.zerock.apiserver.dto.PageRequestDTO;
import org.zerock.apiserver.dto.PageResponseDTO;
import org.zerock.apiserver.dto.ProductDTO;

public interface ProductSearch {

    PageResponseDTO<ProductDTO> searchList(PageRequestDTO pageRequestDTO);

    //Pageable pageable = PageRequest.of(pageRequestDTO);
}
