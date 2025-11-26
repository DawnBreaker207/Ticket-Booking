package com.dawn.backend.config.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class ResponsePage<T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<T> content;

    Pagination pagination;

    public ResponsePage(Page<T> page) {
        this.content = page.getContent();
        this.pagination = new Pagination(
                page.getPageable().getPageNumber(),
                page.getPageable().getPageSize(),
                page.getTotalElements());
    }

    @Data
    static class Pagination {
        Integer pageNumber;
        Integer pageSize;
        Long totalElements;

        public Pagination(Integer pageNumber, Integer pageSize, Long totalElements) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.totalElements = totalElements;
        }
    }
}
