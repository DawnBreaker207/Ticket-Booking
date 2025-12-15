package com.dawn.catalog.dto.request;


import com.dawn.common.constant.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ArticleRequest {
    private String title;

    private String slug;

    private String summary;

    private String content;

    private ArticleStatus status;
}
