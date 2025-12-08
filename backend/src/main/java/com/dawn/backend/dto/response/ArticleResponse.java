package com.dawn.backend.dto.response;

import com.dawn.backend.constant.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ArticleResponse extends BaseResponse {
    private Long id;

    private String title;

    private String slug;

    private String summary;

    private String content;

    private String author;

    private ArticleStatus status;

    private Long views;

    private Boolean isDeleted;
}
