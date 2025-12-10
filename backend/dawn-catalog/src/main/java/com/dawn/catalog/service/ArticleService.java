package com.dawn.backend.service;

import com.dawn.backend.dto.request.ArticleRequest;
import com.dawn.backend.dto.response.ArticleResponse;
import com.dawn.backend.model.Article;

import java.util.List;

public interface ArticleService {
    List<ArticleResponse> getAll();

    ArticleResponse getById(Long id);

    ArticleResponse create(ArticleRequest req);

    ArticleResponse update(Long id, ArticleRequest req);

    Void delete(Long id);
}
