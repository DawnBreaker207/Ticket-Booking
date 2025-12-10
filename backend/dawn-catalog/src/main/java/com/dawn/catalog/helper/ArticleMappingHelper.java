package com.dawn.catalog.helper;


import com.dawn.catalog.dto.request.ArticleRequest;
import com.dawn.catalog.dto.response.ArticleResponse;
import com.dawn.catalog.model.Article;

public interface ArticleMappingHelper {
    static Article map(final ArticleRequest req) {
        return Article
                .builder()
                .title(req.getTitle())
                .slug(req.getSlug())
                .summary(req.getSummary())
                .content(req.getContent())
                .status(req.getStatus())
                .build();
    }

    static ArticleResponse map(final Article req) {
        return ArticleResponse
                .builder()
                .id(req.getId())
                .title(req.getTitle())
                .slug(req.getSlug())
                .summary(req.getSummary())
                .content(req.getContent())
                .author(req.getAuthor().getUsername())
                .status(req.getStatus())
                .views(req.getViews())
                .isDeleted(req.getIsDeleted())
                .build();
    }
}
